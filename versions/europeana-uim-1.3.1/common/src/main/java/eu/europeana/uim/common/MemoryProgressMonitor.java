package eu.europeana.uim.common;

/** Simple memory based implementation of a ProgressMonitor. This class just
 * holds all information in fields and exposes the field values through getter methods.
 * 
 * @author andreas.juffinger@kb.nl
 */
public class MemoryProgressMonitor implements RevisingProgressMonitor {

	private String task ="Not defined";
	private String subtask ="Not defined";
	
	private int work =0;
	private int worked =0;

	private long start;
	
	private boolean cancelled = false;
	

	/** public standard constructor.
	 */
	public MemoryProgressMonitor() {
	}
	
	
	@Override
	public void beginTask(String task, int work) {
	    start = System.currentTimeMillis();
	    
		this.task = task;
		this.work = work;
	}

	@Override
	public void worked(int work) {
		this.worked += work;
	}

	@Override
	public void done() {
		this.worked = work;
	}

	@Override
	public void subTask(String subtask) {
		this.subtask = subtask;
	}

	@Override
	public void setCancelled(boolean canceled) {
		this.cancelled = canceled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @return the task
	 */
	@Override
    public String getTask() {
		return task;
	}

	/**
	 * @return the subtask
	 */
	@Override
    public String getSubtask() {
		return subtask;
	}

	/**
	 * @return the work
	 */
	@Override
    public int getWork() {
		return work;
	}

	/**
	 * @return the worked
	 */
	@Override
    public int getWorked() {
		return worked;
	}


    @Override
    public void setWork(int work) {
        this.work = work;
    }


    @Override
    public void setWorked(int worked) {
        this.worked = worked;
    }


    @Override
    public void setTask(String task) {
        this.task = task;
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
    public void setStart(long start) {
        this.start = start;
    }


    @Override
    public void attached() {
    }


    @Override
    public void detached() {
    }
}
