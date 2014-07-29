/* LinkcheckServer.java - created on Jul 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink.http;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.util.EntityUtils;

import eu.europeana.uim.common.LruCache;
import eu.europeana.uim.guarded.Guarded;

/**
 * HTTP Link checker with internal thread pool using the @see
 * {@link HttpClientSetup} to check the status of links. Initially we try the
 * HEAD method which would be optimal but not widely supported.
 *
 * If the HEAD method is not supported by the webserver we do use as fallback
 * forth on the GET method.
 *
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jul 15, 2011
 */
public class WeblinkLinkchecker extends AbstractWeblinkServer {

    private static final Logger log = Logger.getLogger(WeblinkLinkchecker.class.getName());

    private static WeblinkLinkchecker instance;

    private final LruCache<String, String> head = new LruCache<>(1000);

    /**
     * @return the singleton linkchecker instance
     */
    public static WeblinkLinkchecker getShared() {
        if (instance == null) {
            instance = new WeblinkLinkchecker();
        }
        return instance;
    }

    /**
     * Private singleton constructor.
     */
    public WeblinkLinkchecker() {
    }

    @Override
    public void shutdown() {
        super.shutdown();
        instance = null;
    }

    @Override
    public Runnable createTask(Guarded guarded) {
        return new CheckTask((GuardedMetaDataRecordUrl<?>) guarded);
    }

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 15, 2011
     */
    private class CheckTask implements Runnable {

        private final GuardedMetaDataRecordUrl<?> guarded;

        public CheckTask(GuardedMetaDataRecordUrl<?> guarded) {
            this.guarded = guarded;
        }

        @Override
        public void run() {
            if (guarded != null) {
                Submission submission = getSubmission(guarded.getExecution());

                synchronized (head) {
                    if (!head.containsKey(guarded.getUrl().getHost())) {
                        head.put(guarded.getUrl().getHost(), "HEAD");
                    }
                }

                int status = 0;
                HttpResponse response = null;
                HttpRequestBase http = null;

                if ("HEAD".equals(head.get(guarded.getUrl().getHost()))) {
                    http = new HttpHead(guarded.getUrl().toExternalForm());
                } else {
                    http = new HttpGet(guarded.getUrl().toExternalForm());
                }

                try {
                    response = HttpClientSetup.getHttpClient().execute(http);
                    status = response.getStatusLine().getStatusCode();

                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_METHOD_NOT_ALLOWED
                            && http instanceof HttpHead) {
                        EntityUtils.consume(response.getEntity());

                        http = new HttpGet(guarded.getUrl().toExternalForm());
                        response = HttpClientSetup.getHttpClient().execute(http);
                        status = response.getStatusLine().getStatusCode();

                        synchronized (head) {
                            head.put(guarded.getUrl().getHost(), "GET");
                        }
                    }

                    if (status == HttpStatus.SC_MOVED_TEMPORARILY
                            || status == HttpStatus.SC_MOVED_PERMANENTLY) {
                        EntityUtils.consume(response.getEntity());

                        Header locationHeader = response.getFirstHeader("location");
                        if (locationHeader != null) {
                            String location = locationHeader.getValue();

                            http = new HttpGet(location);
                            response = HttpClientSetup.getHttpClient().execute(http);
                            status = response.getStatusLine().getStatusCode();
                        }

                    } else if (status == HttpStatus.SC_BAD_REQUEST
                            || status == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                        log.log(Level.INFO, "Bad request: <{0}>.", guarded.getUrl());
                    }

                } catch (Throwable t) {
                    synchronized (submission) {
                        submission.incrExceptions();
                    }

                    log.log(Level.INFO, "Failed to load url: <" + guarded.getUrl() + ">.", t);
                    guarded.processed(0, t.getLocalizedMessage());

                } finally {
                    try {
                        if (response != null) {
                            if (response.getEntity() != null) {
                                InputStream content = response.getEntity().getContent();
                                if (content != null) {
                                    if (content instanceof EofSensorInputStream) {
                                        ((EofSensorInputStream) content).abortConnection();
                                    }
                                }
                            } else {
                                EntityUtils.consume(response.getEntity());
                            }
                        }

                        synchronized (submission) {
                            submission.incrStatus(status);
                            submission.incrProcessed();
                            submission.removeRemaining(guarded);
                        }

                        // just do if no exception happened
                        if (status > 0) {
                            String message = response == null ? "No response."
                                    : response.getStatusLine().getReasonPhrase();
                            guarded.processed(status, message);

                        } else {

                            // already handled within catch clause
                            log.log(Level.INFO, "Status was zero: <{0}>.", guarded.getUrl());
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

}
