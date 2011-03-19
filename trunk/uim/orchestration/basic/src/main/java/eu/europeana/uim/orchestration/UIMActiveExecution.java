package eu.europeana.uim.orchestration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.common.RevisableProgressMonitor;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;
import eu.europeana.uim.workflow.WorkflowStepStatus;

public class UIMActiveExecution implements ActiveExecution<Task> {
    private static Logger                     log       = Logger.getLogger(UIMActiveExecution.class.getName());

    private HashMap<String, LinkedList<Task>> success   = new LinkedHashMap<String, LinkedList<Task>>();
    private HashMap<String, LinkedList<Task>> failure   = new LinkedHashMap<String, LinkedList<Task>>();
    private HashMap<String, HashSet<Task>>    assigned  = new LinkedHashMap<String, HashSet<Task>>();

    private HashMap<TKey<?, ?>, Object>       values    = new HashMap<TKey<?, ?>, Object>();

    private final StorageEngine               storageEngine;
    private final LoggingEngine<?>            loggingEngine;

    private final Execution                   execution;
    private final Workflow                    workflow;
    private final Properties                  properties;
    private final RevisableProgressMonitor    monitor;

    private boolean                           paused;
    private Throwable                         throwable;

    private int                               scheduled = 0;

    private int                               completed = 0;

    public UIMActiveExecution(Execution execution, Workflow workflow, StorageEngine storageEngine,
                              LoggingEngine loggingEngine, Properties properties,
                              RevisableProgressMonitor monitor) {
        this.execution = execution;
        this.workflow = workflow;
        this.storageEngine = storageEngine;
        this.loggingEngine = loggingEngine;
        this.properties = properties;
        this.monitor = monitor;

        WorkflowStart start = workflow.getStart();
        success.put(start.getName(), new LinkedList<Task>());
        failure.put(start.getName(), new LinkedList<Task>());
        assigned.put(start.getName(), new HashSet<Task>());

        for (IngestionPlugin step : workflow.getSteps()) {
            success.put(step.getName(), new LinkedList<Task>());
            failure.put(step.getName(), new LinkedList<Task>());
            assigned.put(step.getName(), new HashSet<Task>());
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

    public Date getCancelTime() {
        return execution.getCancelTime();
    }

    public void setCancelTime(Date end) {
        execution.setCancelTime(end);
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
    public RevisableProgressMonitor getMonitor() {
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
    public Set<Task> getAssigned(String identifier) {
        return assigned.get(identifier);
    }

    @Override
    public void incrementCompleted(int work) {
        completed += work;
    }

    @Override
    public int getProgressSize() {
        int size = 0;
        WorkflowStart start = getWorkflow().getStart();
        size += getProgressSize(start.getName());

        for (IngestionPlugin step : getWorkflow().getSteps()) {
            size += getProgressSize(step.getName());
        }
        return size;
    }

    private int getProgressSize(String name) {
        int size = 0;
        LinkedList<Task> list = success.get(name);
        synchronized (list) {
            size += list.size();

            HashSet<Task> set = assigned.get(name);
            size += set.size();
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
        if (scheduled % 2500 == 0) {
            if (log.isLoggable(Level.INFO)) {
                int totalProgress = 0;
                StringBuilder builder = new StringBuilder();

                WorkflowStart start = getWorkflow().getStart();
                int startSize = getProgressSize(start.getName());
                totalProgress += startSize;
                builder.append(start.getName());
                builder.append("=");
                builder.append(startSize);
                builder.append(", ");

                for (IngestionPlugin step : getWorkflow().getSteps()) {
                    int stepSize = getProgressSize(step.getName());
                    totalProgress += stepSize;
                    builder.append(step.getName());
                    builder.append("=");
                    builder.append(stepSize);
                    builder.append(", ");
                }

                log.info(scheduled + " scheduled, " + totalProgress + " in progress:" +
                         builder.toString());
            }
        }
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
        boolean cancelled = getMonitor().isCancelled();

        boolean finished = getWorkflow().getStart().isFinished(this, getStorageEngine());

        boolean processed = getScheduledSize() == getFailureSize() + getCompletedSize();
        boolean empty = getProgressSize() == 0;

        // System.out.println(String.format("s=%d, p=%d, f=%d, c=%d", getScheduledSize(),
// getProgressSize(), getFailureSize(), getCompletedSize()));
        return (finished || cancelled) && processed && empty;
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
        Queue<Task> success = getSuccess(step.getName());
        Queue<Task> failure = getFailure(step.getName());

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

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        } while (getExecution().isActive());

        // give it the time to store if necessary.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

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
