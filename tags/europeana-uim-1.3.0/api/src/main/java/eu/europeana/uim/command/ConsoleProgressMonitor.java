package eu.europeana.uim.command;

import java.io.PrintStream;

import eu.europeana.uim.common.RevisingProgressMonitor;

/**
 * Prints out progress to the console.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class ConsoleProgressMonitor implements RevisingProgressMonitor {
    private final PrintStream out;

    private boolean           cancelled = false;
    private int               worked    = 0;

    private int               work;

    private long              start;

    private String            task;

    private String            subtask;

    /**
     * Creates a new instance of this class.
     * 
     * @param out
     *            prints progress to the given stream
     */
    public ConsoleProgressMonitor(PrintStream out) {
        super();
        this.out = out;
    }

    @Override
    public void attached() {
        out.print("Attached to monitor current status: <" + task + "> total " + work +
                  " units of work. Worked so far:" + getWorked() + " [");
    }

    @Override
    public void detached() {
        out.print("Detached to monitor current status:" + task + ", " + work +
                  " units of work. Worked so far:" + getWorked());
    }

    @Override
    public void beginTask(String task, int work) {
        setTask(task);
        setWork(work);
        out.print("Starting:" + task + ", " + work + " units of work. [");
    }

    @Override
    public void worked(int work) {
        out.print(".");
        worked += work;
        if (worked % 10 == 0) {
            out.print("|");
        }
    }

    @Override
    public void done() {
        out.println("]");
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
}
