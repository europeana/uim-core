package org.theeuropeanlibrary.uim.gui.gwt.server;

import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

import eu.europeana.uim.common.RevisingProgressMonitor;

/**
 * GWT implementation of a ProgressMonitor. Since we display things on the client and the monitor is
 * on the server, we have to pass through an intermediary model (the Execution). We need to poll it
 * from the client, this is why we update it here. (Once WebSockets are standard, we'll be able to
 * use those).
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 1, 2011
 */
public class GWTProgressMonitor implements RevisingProgressMonitor, IsSerializable {
    @SuppressWarnings("unused")
    private String       name;
    private int          work;
    private int          worked;

    private long         start;

    private boolean      cancelled;
    private ExecutionDTO execution;

    private String       task;
    private String       subtask;

    /**
     * Creates a new instance of this class.
     */
    public GWTProgressMonitor() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     */
    public GWTProgressMonitor(ExecutionDTO execution) {
        this.execution = execution;
    }

    @Override
    public void beginTask(String task, int work) {
        this.name = task;
        this.work = work;
        this.worked = 0;
    }

    @Override
    public void worked(int work) {
        if (worked + work > work) {
            worked = work;
            done();
        } else {
            this.worked = worked + work;
        }
        // update the completed status of the execution
        // here we make no difference between success and failure
        // we just log everything as "completed"
        execution.setCompleted(worked);
    }

    @Override
    public void done() {
        execution.setCanceled(false);
        execution.setActive(false);
    }

    @Override
    public void subTask(String subtask) {
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if (cancelled) {
            execution.setCanceled(true);
            execution.setActive(false);
        }
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @return done?
     */
    public boolean isDone() {
        return !execution.isActive() && !execution.isCanceled();
    }

    @Override
    public int getWork() {
        return work;
    }

    @Override
    public void setWork(int work) {
        this.work = work;
    }

    @Override
    public int getWorked() {
        return worked;
    }

    @Override
    public void setWorked(int worked) {
        this.worked = worked;
    }

    @Override
    public String getTask() {
        return task;
    }

    @Override
    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String getSubtask() {
        return subtask;
    }

    @Override
    public void setSubtask(String subtask) {
        this.subtask = subtask;
    }

    @Override
    public long getStart() {
        return start;
    }

    @Override
    public void setStart(long millis) {
        this.start = millis;
    }

    @Override
    public void attached() {
    }

    @Override
    public void detached() {
    }
}
