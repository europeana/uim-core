package eu.europeana.uim.store.memory;

import java.util.Date;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;

public class MemoryExecution implements Execution {

	private long id;

    private boolean active = false;
    private Date startTime, endTime;
    private DataSet dataSet;
    private String workflowIdentifier;

    public MemoryExecution() {
		super();
	}

	public MemoryExecution(long id) {
		this.id = id;
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

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getWorkflowName() {
        return workflowIdentifier;
    }

    @Override
    public void setWorkflowName(String identifier) {
        this.workflowIdentifier = identifier;
    }

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "MemoryExecution [id=" + id + ", workflowIdentifier="
				+ workflowIdentifier + ", dataSet=" + dataSet + "]";
	}
	
	
}
