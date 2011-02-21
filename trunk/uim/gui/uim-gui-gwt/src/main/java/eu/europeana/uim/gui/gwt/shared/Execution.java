package eu.europeana.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import eu.europeana.uim.common.MemoryProgressMonitor;

import java.util.Date;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class Execution implements IsSerializable, Comparable<Execution> {

    private Long id;
    private String name;
    private String workflow;
    
    private Progress progress;
    
    private int scheduled;
    private int completed;
    private int failure;
    
    private boolean isActive;
    private Date startTime;
    private Date endTime;
	private String dataset;
	private boolean done;

    public Execution() {
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }
    
    public String getDataSet() {
    	return dataset;
    }
    
    public void setDataSet(String dataset) {
    	this.dataset= dataset;
    }
    
    

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public int getScheduled() {
		return scheduled;
	}

    public void setScheduled(int scheduled) {
		this.scheduled = scheduled;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getFailure() {
		return failure;
	}

	public void setFailure(int failure) {
		this.failure = failure;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isDone() {
        return done;
    }


    @Override
    public int compareTo(Execution execution) {
        return execution.getStartTime().compareTo(this.getStartTime());
    }
}
