package eu.europeana.uim.orchestration.processing;

import java.util.Hashtable;

/**
 * This represents a thread for a task executor.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class TaskExecutorThread extends Thread {

    /**
     * context for this thread to hold thread specific information
     */
    protected Hashtable<String, Object> threadContext = new Hashtable<String, Object>();

    /**
     * Creates a new instance of this class.
     * 
     * @param group
     * @param runable
     * @param name
     */
    public TaskExecutorThread(ThreadGroup group, Runnable runable, String name) {
        super(group, runable, name);
    }

    /**
     * @return Returns the threadContext.
     */
    public Hashtable<String, Object> getThreadContext() {
        return threadContext;
    }
}