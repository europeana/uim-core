package eu.europeana.uim.gui.gwt.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.gui.gwt.shared.Execution;

import java.util.logging.Logger;

/**
 * GWT implementation of a ProgressMonitor. Since we display things on the client and the monitor is on the server,
 * we have to pass through an intermediary model (the Execution). We need to poll it from the client, this is why
 * we update it here. (Once WebSockets are standard, we'll be able to use those).
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class GWTProgressMonitor implements ProgressMonitor, IsSerializable {

    private static Logger log = Logger.getLogger(GWTProgressMonitor.class.getName());


    private String name;
    private int work;
    private int worked;

    private boolean cancelled;
    private Execution execution;

    public GWTProgressMonitor() {
    }

    public GWTProgressMonitor(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void beginTask(String task, int work) {
        this.name = task;
        this.work = work;
        this.worked = 0;
        
        
        execution.getProgress().setTask(task);
        execution.getProgress().setWork(work);
    }

    
    @Override
    public void worked(int work) {
        if (worked + work > work) {
            worked = work;
            done();
        } else {
            this.worked = worked + work;
        }
        execution.getProgress().setWorked(worked);
    }

    @Override
    public void done() {
        execution.getProgress().setDone(true);
        execution.setActive(false);
    }

    @Override
    public void subTask(String subtask) {
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDone() {
        return execution.isDone();
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }
}
