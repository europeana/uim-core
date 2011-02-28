/* LoaderRunnable.java - created on Feb 28, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.workflow;

import java.util.Queue;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 28, 2011
 */
public abstract class TaskCreator implements Runnable {

    private boolean done  = false;
    private Queue<Task> queue;

    public void setDone(boolean done) {
        this.done = done;
    }
    
    public boolean isDone() {
        return done;
    }

    public void setQueue(Queue<Task> queue) {
        this.queue = queue;
    }

    public Queue<Task> getQueue() {
        return queue;
    }
    
}
