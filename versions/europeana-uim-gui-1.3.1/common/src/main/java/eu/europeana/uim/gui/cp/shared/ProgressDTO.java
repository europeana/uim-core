package eu.europeana.uim.gui.cp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Object to represent progress of an execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class ProgressDTO implements IsSerializable {
    private String  task    = "Not defined";
    private String  subtask = "Not defined";

    private int     work    = 0;
    private int     worked  = 0;

    private boolean done    = false;

    /**
     * public standard constructor.
     */
    public ProgressDTO() {
    }

    /**
     * @return task
     */
    public String getTask() {
        return task;
    }

    /**
     * @param task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @return sub task
     */
    public String getSubtask() {
        return subtask;
    }

    /**
     * @param subtask
     */
    public void setSubtask(String subtask) {
        this.subtask = subtask;
    }

    /**
     * @return work
     */
    public int getWork() {
        return work;
    }

    /**
     * @param work
     */
    public void setWork(int work) {
        this.work = work;
    }

    /**
     * @return worked
     */
    public int getWorked() {
        return worked;
    }

    /**
     * @param worked
     */
    public void setWorked(int worked) {
        this.worked = worked;
    }

    /**
     * @return done?
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @param done
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
