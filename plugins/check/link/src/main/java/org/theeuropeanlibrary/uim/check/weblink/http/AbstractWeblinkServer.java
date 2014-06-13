/* LinkcheckServer.java - created on Jul 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.common.SimpleThreadFactory;
import eu.europeana.uim.guarded.Guarded;
import eu.europeana.uim.guarded.GuardedQueue;
import eu.europeana.uim.guarded.TimedDifferenceCondition;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

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
public abstract class AbstractWeblinkServer {

    private static final Logger log = Logger.getLogger(AbstractWeblinkServer.class.getName());

    private ThreadPoolExecutor executor;
    private final GuardedQueue guard;
    private final Scheduler scheduler;

    private final Map<Object, Submission> submissions = new HashMap<>();

    /**
     * Creates a new instance of this class.
     */
    protected AbstractWeblinkServer() {
        // according to robots protocol one should not hit a server more often
        // than 2-3 times per second.
        TimedDifferenceCondition condition = new TimedDifferenceCondition(800);

        // this setting allows up to 50 parallel servers and a queue of maximum
        // about 100 per server.
        guard = new GuardedQueue(condition, 50, 1000000, 50000000, true);

        executor = new ThreadPoolExecutor(25, 50, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new SimpleThreadFactory("weblink", "linkcheck"));

        scheduler = new Scheduler();
        new Thread(scheduler).start();
    }

    /**
     * Method to offer a new guarded url to the linkchecker. The url is added to
     * the queue and we immediately return. the execution context is used to
     * create or lookup the submission instance holding statistical information.
     *
     * @param <I>
     *
     * @param guarded
     * @param context
     */
    public synchronized <I> void offer(GuardedMetaDataRecordUrl<I> guarded,
            ExecutionContext<MetaDataRecord<I>, I> context) {
        synchronized (submissions) {
            if (!submissions.containsKey(context.getExecution().getId())) {
                submissions.put(context.getExecution().getId(), new Submission(
                        ((ActiveExecution<MetaDataRecord<I>, I>) context).getStorageEngine(), guard));
            }
        }

        boolean offer = guard.offer(guarded);
        if (offer) {
            Submission submission = submissions.get(context.getExecution().getId());
            synchronized (submission) {
                submission.addRemaining(guarded);
            }
        }

    }

    /**
     * @param guarded
     * @return the task which does the job
     */
    public abstract Runnable createTask(Guarded guarded);

    /**
     * Method to get the submission object with a bit more detailed runtime
     * information about the linkcheck progress for this execution.
     *
     * @param execution
     * @return submission or null for the given execution.
     */
    public Submission getSubmission(Execution<?> execution) {
        return submissions.get(execution.getId());
    }

    /**
     * Method to nicely shutdown the linkchecker. This method stops the internal
     * scheduler and also the attached thread pool executor.
     */
    public void shutdown() {
        scheduler.running = false;
        while (!scheduler.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        executor.shutdown();
        submissions.clear();
    }

    /**
     * Simple runnable which creates out of the links in the guarded queu the
     * necessary runnables to be offered to the task pool executor.
     *
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 15, 2011
     */
    private class Scheduler implements Runnable {

        private boolean running = true;
        private boolean done = false;

        public Scheduler() {
        }

        @Override
        public void run() {
            long sleep = 5;
            while (running) {
                try {
                    Guarded guarded = guard.poll(500, TimeUnit.MILLISECONDS);
                    if (guarded != null) {
                        Runnable task = createTask(guarded);
                        try {
                            executor.execute(task);
                            Thread.sleep(sleep);
                            if (sleep > 5) {
                                sleep--;
                            }
                        } catch (IllegalThreadStateException t) {
                            log.log(Level.SEVERE,
                                    "Linkchecker thread pool is dead. Starting a new pool.");
                            executor.shutdownNow();

                            executor = new ThreadPoolExecutor(25, 50, 60L, TimeUnit.SECONDS,
                                    new SynchronousQueue<Runnable>(), new SimpleThreadFactory(
                                            "weblink", "linkcheck"));
                            executor.execute(task);

                        } catch (RejectedExecutionException exc) {
                            // we ran out of worker threads
                            // slow down a bit ...
                            Thread.sleep(5000);
                            if (sleep < 100) {
                                sleep = sleep * 2;
                            }
                        }
                    } else {
                        log.log(Level.FINE, "No guarded available.");
                    }
                } catch (Throwable t) {
                    log.log(Level.SEVERE, "Failed to schedule web link task", t);
                }
            }
            done = true;
        }

        /**
         * Returns the done.
         *
         * @return the done
         */
        public boolean isDone() {
            return done;
        }
    }
}
