/* LoaderRunnable.java - created on Feb 28, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.plugin.source;

import java.util.Queue;

import eu.europeana.uim.store.UimDataSet;

/**
 * This class extends Runnable to create new tasks generic of which kind of data
 * set or identifier to be used
 *
 * @param <U> generic data set
 * @param <I> generic identifier
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 28, 2011
 */
public abstract class TaskCreator<U extends UimDataSet<I>, I> implements Runnable {

    private boolean done = false;
    private Queue<Task<U, I>> queue;

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
     * @param queue holding queued tasks to add new ones
     */
    public void setQueue(Queue<Task<U, I>> queue) {
        this.queue = queue;
    }

    /**
     * @return queue holding queued tasks to add new ones
     */
    public Queue<Task<U, I>> getQueue() {
        return queue;
    }
}
