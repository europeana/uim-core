package eu.europeana.uim.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * Factory pattern to provide newly created threads for task execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class SimpleThreadFactory implements ThreadFactory {
    private static long              id         = 0;

    private String                   groupname  = "anonymouse";
    private String                   threadname = "anonymouse";

    private ThreadGroup              threadgroup;
    private UncaughtExceptionHandler exceptionhandler;

    /**
     * Creates a new instance of this class.
     */
    public SimpleThreadFactory() {
        threadgroup = new ThreadGroup(groupname);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param threadname
     */
    public SimpleThreadFactory(String threadname) {
        this.threadname = threadname;

        threadgroup = new ThreadGroup(groupname);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupname
     * @param threadname
     */
    public SimpleThreadFactory(String groupname, String threadname) {
        this.groupname = groupname;
        this.threadname = threadname;

        threadgroup = new ThreadGroup(groupname);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread worker = new Thread(threadgroup, runnable, threadname + "[" + id++ + "]");
        worker.setUncaughtExceptionHandler(exceptionhandler);
        return worker;
    }

    /**
     * @return the groupname
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * @return name of thread
     */
    public String getThreadname() {
        return threadname;
    }

    /**
     * @return handler for exceptions
     */
    public UncaughtExceptionHandler getExceptionHandler() {
        return exceptionhandler;
    }

    /**
     * @param exceptionhandler
     */
    public void setExceptionHandler(UncaughtExceptionHandler exceptionhandler) {
        this.exceptionhandler = exceptionhandler;
    }
}
