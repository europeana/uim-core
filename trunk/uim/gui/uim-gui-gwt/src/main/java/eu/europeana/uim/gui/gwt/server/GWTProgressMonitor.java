package eu.europeana.uim.gui.gwt.server;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;
import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.gui.gwt.shared.Execution;

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
        execution.setDone(true);
        execution.setActive(false);
    }

    @Override
    public void subTask(String subtask) {
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if(cancelled) {
            execution.setActive(false);
        }
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDone() {
        return execution.isDone();
    }

}
