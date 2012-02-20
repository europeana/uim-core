/* HttpClientSetup.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink.http;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * Function class holding a {@link HttpClient} together with creation and guarded queue.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class HttpClientSetup {
    private static HttpClient                  client;
    private static ThreadSafeClientConnManager manager;

    /**
     * @return reference to a configured http client
     */
    public static HttpClient getHttpClient() {
        if (client == null) {
            client = createHttpClient();
        }
        return client;
    }

    private static HttpClient createHttpClient() {
        manager = new ThreadSafeClientConnManager();
        manager.setMaxTotal(250);

        HttpParams parameters = new BasicHttpParams();

        // HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(parameters, HTTP.UTF_8);
        HttpProtocolParams.setUserAgent(parameters, "The European Library: Validation");
        parameters.setParameter(HttpMethodParams.STRICT_TRANSFER_ENCODING,
                new Boolean(false));
        parameters.setParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, new Boolean(true));
        parameters.setParameter(HttpMethodParams.UNAMBIGUOUS_STATUS_LINE,
                new Boolean(false));
        parameters.setIntParameter(HttpMethodParams.STATUS_LINE_GARBAGE_LIMIT, 10);

        // some webservers have problems if this is set to true
        HttpProtocolParams.setUseExpectContinue(parameters, false);
        HttpConnectionParams.setConnectionTimeout(parameters, 10000);
        HttpConnectionParams.setSoTimeout(parameters, 10000);

        
        HttpClient client = new DefaultHttpClient(manager, parameters);

        // Configure how we want the method to act.
        return client;
    }

    /**
     * shutdown the connection
     */
    public static void shutdown() {
        MultiThreadedHttpConnectionManager.shutdownAll();
        client = null;
    }
}
