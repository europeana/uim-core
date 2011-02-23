package eu.europeana.uim.orchestration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;
import eu.europeana.uim.workflow.WorkflowStep;
import eu.europeana.uim.workflow.WorkflowStepStatus;

public class UIMActiveExecution implements ActiveExecution<Task> {

	private HashMap<String, LinkedList<Task>> success = new LinkedHashMap<String, LinkedList<Task>>();
	private HashMap<String, LinkedList<Task>> failure = new LinkedHashMap<String, LinkedList<Task>>();

	private HashMap<String, HashMap<String, Object>> values = new HashMap<String, HashMap<String, Object>>();

	private final StorageEngine storageEngine;
    private final LoggingEngine<?> loggingEngine;

	private final Execution execution;
	private final Workflow workflow;
	private final Properties properties;
	private final ProgressMonitor monitor;

	private boolean paused;
	private Throwable throwable;

	private int scheduled = 0;

	
	private int completed = 0;

	public UIMActiveExecution(Execution execution, Workflow workflow, StorageEngine storageEngine, LoggingEngine loggingEngine, Properties properties, ProgressMonitor monitor){
		this.execution = execution;
		this.workflow = workflow;
		this.storageEngine = storageEngine;
        this.loggingEngine = loggingEngine;
        this.properties = properties;
        this.monitor = monitor;

		WorkflowStart start = workflow.getStart();
		success.put(start.getIdentifier(), new LinkedList<Task>());
		failure.put(start.getIdentifier(), new LinkedList<Task>());

		for (WorkflowStep step : workflow.getSteps()) {
			success.put(step.getIdentifier(), new LinkedList<Task>());
			failure.put(step.getIdentifier(), new LinkedList<Task>());
		}
	}

    @Override
    public Execution getExecution() {
        return this;
    }

    @Override
	public StorageEngine getStorageEngine() {
		return storageEngine;
	}

    @Override
    public LoggingEngine<?> getLoggingEngine() {
        return loggingEngine;
    }
    
    @Override
	public Properties getProperties() {
		return properties;
	}

	public long getId() {
		return execution.getId();
	}

	public boolean isActive() {
		return execution.isActive();
	}

	public void setActive(boolean active) {
		execution.setActive(active);
	}

	public Date getStartTime() {
		return execution.getStartTime();
	}

	public void setStartTime(Date start) {
		execution.setStartTime(start);
	}

	public Date getEndTime() {
		return execution.getEndTime();
	}

	public void setEndTime(Date end) {
		execution.setEndTime(end);
	}

	public DataSet getDataSet() {
		return execution.getDataSet();
	}

	public void setDataSet(DataSet entity) {
		execution.setDataSet(entity);
	}

	public String getWorkflowName() {
		return execution.getWorkflowName();
	}

	public void setWorkflowName(String name) {
		execution.setWorkflowName(name);
	}

	@Override
	public ProgressMonitor getMonitor() {
		return monitor;
	}


	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public Throwable getThrowable() {
		return throwable;
	}


	@Override
	public Queue<Task> getSuccess(String identifier) {
		return success.get(identifier);
	}

	@Override
	public Queue<Task> getFailure(String identifier) {
		return failure.get(identifier);
	}

	@Override
	public void done(int count) {
		completed += count;
	}


	@Override
	public int getProgressSize() {
		int size = 0;
		for (LinkedList<Task> tasks : success.values()) {
			size += tasks.size();
		}

		return size;
	}

	@Override
	public int getFailureSize() {
		// count elements in failure queues
		int size = 0;
		for (LinkedList<Task> tasks : failure.values()) {
			size += tasks.size();
		}
		return size;
	}


	@Override
	public int getScheduledSize() {
		return scheduled;
	}

	@Override
	public void incrementScheduled(int work) {
		scheduled += work;
	}

    @Override
    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }

    @Override
	public int getCompletedSize() {
		return completed;
	}


	@Override
	public boolean isFinished() {
		boolean finished = getWorkflow().getStart().isFinished(this);
		boolean processed = getWorkflow().getStart().getScheduledSize() == getFailureSize() + getCompletedSize();
		return finished && processed;
	}





	@Override
	public List<WorkflowStepStatus> getStepStatus() {
		List<WorkflowStepStatus> status = new ArrayList<WorkflowStepStatus>();
		for (WorkflowStep step : getWorkflow().getSteps()) {
			status.add(getStepStatus(step));
		}
		return status;
	}


	public WorkflowStepStatus getStepStatus(WorkflowStep step) {
		Queue<Task> success = getSuccess(step.getIdentifier());
		Queue<Task> failure = getFailure(step.getIdentifier());
		
		int successSize = 0;
		synchronized(success) {
			successSize = success.size();
		}
		
		int failureSize = 0;
		Map<MetaDataRecord, Throwable> exceptions = new HashMap<MetaDataRecord, Throwable>();
		synchronized(failure) {
			failureSize = failure.size();
			for (Task task : failure) {
				exceptions.put(task.getMetaDataRecord(), task.getThrowable());
			}
		}

		WorkflowStepStatus status = new UIMWorkflowStepStatus(step, successSize, failureSize, exceptions);
		return status;
	}

	
//	@Override
//	public long[] nextBatch() {
//		if (batches.isEmpty()) return null;
//		return batches.remove(0);
//	}
//
//	@Override
//	public void addBatch(long[] ids) {
//		batches.add(ids);
//		scheduled += ids.length;
//	}


	@Override
	public Workflow getWorkflow() {
		return workflow;
	}


	@Override
	public void waitUntilFinished() {
		while (!isFinished()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		System.out.println("Finished:" + getCompletedSize());
		System.out.println("Failed:" + getFailureSize());
	}

	@Override
	public void putValue(WorkflowStep step, String key, Object value) {
		if (!values.containsKey(step.getIdentifier())) {
			values.put(step.getIdentifier(), new HashMap<String, Object>());
		}
		values.get(step.getIdentifier()).put(key, value);
	}

	@Override
	public Object getValue(WorkflowStep step, String key) {
		if (!values.containsKey(step.getIdentifier())) return null;
		return values.get(step.getIdentifier()).get(key);
	}


	@Override
	public void putValue(IngestionPlugin plugin, String key, Object value) {
		if (!values.containsKey(plugin.getIdentifier())) {
			values.put(plugin.getIdentifier(), new HashMap<String, Object>());
		}
		values.get(plugin.getIdentifier()).put(key, value);
	}

	@Override
	public Object getValue(IngestionPlugin plugin, String key) {
		if (!values.containsKey(plugin.getIdentifier())) return null;
		return values.get(plugin.getIdentifier()).get(key);
	}




}
