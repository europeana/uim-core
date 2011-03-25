/* LoaderRunnable.java - created on Feb 28, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.workflow;

import java.util.Queue;

/**
 * This class extends Runnable to create new tasks.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 28, 2011
 */
public abstract class TaskCreator implements Runnable {
    private boolean     done = false;
    private Queue<Task> queue;

    /**
     * @param done
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * @return done?
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @param queue
     *            holding queued tasks to add new ones
     */
    public void setQueue(Queue<Task> queue) {
        this.queue = queue;
    }

    /**
     * @return queue holding queued tasks to add new ones
     */
    public Queue<Task> getQueue() {
        return queue;
    }
}
