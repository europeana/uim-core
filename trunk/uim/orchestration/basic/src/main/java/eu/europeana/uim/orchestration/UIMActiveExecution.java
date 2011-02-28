package eu.europeana.uim.orchestration;

import java.io.Serializable;
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
import eu.europeana.uim.TKey;
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
import eu.europeana.uim.workflow.WorkflowStepStatus;

public class UIMActiveExecution implements ActiveExecution<Task> {

    private HashMap<String, LinkedList<Task>> success   = new LinkedHashMap<String, LinkedList<Task>>();
    private HashMap<String, LinkedList<Task>> failure   = new LinkedHashMap<String, LinkedList<Task>>();

    private HashMap<TKey<?, ?>, Object>       values    = new HashMap<TKey<?, ?>, Object>();

    private final StorageEngine               storageEngine;
    private final LoggingEngine<?>            loggingEngine;

    private final Execution                   execution;
    private final Workflow                    workflow;
    private final Properties                  properties;
    private final ProgressMonitor             monitor;

    private boolean                           paused;
    private Throwable                         throwable;

    private int                               scheduled = 0;

    private int                               completed = 0;

    public UIMActiveExecution(Execution execution, Workflow workflow, StorageEngine storageEngine,
                              LoggingEngine loggingEngine, Properties properties,
                              ProgressMonitor monitor) {
        this.execution = execution;
        this.workflow = workflow;
        this.storageEngine = storageEngine;
        this.loggingEngine = loggingEngine;
        this.properties = properties;
        this.monitor = monitor;

        WorkflowStart start = workflow.getStart();
        success.put(start.getClass().getSimpleName(), new LinkedList<Task>());
        failure.put(start.getClass().getSimpleName(), new LinkedList<Task>());

        for (IngestionPlugin step : workflow.getSteps()) {
            success.put(step.getClass().getSimpleName(), new LinkedList<Task>());
            failure.put(step.getClass().getSimpleName(), new LinkedList<Task>());
        }
    }

    @Override
    public Execution getExecution() {
        return execution;
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
    public int getCompletedSize() {
        return completed;
    }

    @Override
    public int getTotalSize() {
        return getWorkflow().getStart().getTotalSize(this);
    }


    @Override
    public boolean isFinished() {
        boolean finished = getWorkflow().getStart().isFinished(this, getStorageEngine());
        boolean processed = getScheduledSize() == getFailureSize() + getCompletedSize();
        return finished && processed;
    }

    @Override
    public List<WorkflowStepStatus> getStepStatus() {
        List<WorkflowStepStatus> status = new ArrayList<WorkflowStepStatus>();
        for (IngestionPlugin step : getWorkflow().getSteps()) {
            status.add(getStepStatus(step));
        }
        return status;
    }

    public WorkflowStepStatus getStepStatus(IngestionPlugin step) {
        Queue<Task> success = getSuccess(step.getClass().getSimpleName());
        Queue<Task> failure = getFailure(step.getClass().getSimpleName());

        int successSize = 0;
        synchronized (success) {
            successSize = success.size();
        }

        int failureSize = 0;
        Map<MetaDataRecord, Throwable> exceptions = new HashMap<MetaDataRecord, Throwable>();
        synchronized (failure) {
            failureSize = failure.size();
            for (Task task : failure) {
                exceptions.put(task.getMetaDataRecord(), task.getThrowable());
            }
        }

        WorkflowStepStatus status = new UIMWorkflowStepStatus(step, successSize, failureSize,
                exceptions);
        return status;
    }


    @Override
    public Workflow getWorkflow() {
        return workflow;
    }

    @Override
    public void waitUntilFinished() {
        int count = 0;
        do {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            
            if (isFinished()) {
                count++;
            } else {
                count = 0;
            }
        } while (count < 3);

        System.out.println("Finished:" + getCompletedSize());
        System.out.println("Failed:" + getFailureSize());
    }

    
    @Override
    public <NS, T extends Serializable> void putValue(TKey<NS, T> key, T value) {
        values.put(key, value);
    }

    @Override
    public <NS, T extends Serializable> T getValue(TKey<NS, T> key) {
        Object object = values.get(key);
        if (object != null) { return (T)object; }
        return null;
    }

}
