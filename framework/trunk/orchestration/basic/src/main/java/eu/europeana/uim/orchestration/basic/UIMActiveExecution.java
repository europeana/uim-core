package eu.europeana.uim.orchestration.basic;

import java.io.File;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
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

import org.apache.commons.io.FileUtils;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.common.progress.RevisableProgressMonitor;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.plugin.ExecutionPlugin;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * Active execution implementation specific for UIM workflow.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class UIMActiveExecution<U extends UimDataSet<I>, I> implements ActiveExecution<U, I> {
    /**
     * UIMActiveExecution KEEP_TMP_FILES_AFTER_EXECUTION_KEY if set to true, the directory with the
     * temporary files is not deleted after the execution.
     **/
    public static String                            KEEP_TMP_FILES_AFTER_EXECUTION_KEY = "execution.keepTmpFilesAfterExecution";

    private static Logger                           log                                = Logger.getLogger(UIMActiveExecution.class.getName());

    private HashMap<String, LinkedList<Task<U, I>>> success                            = new LinkedHashMap<String, LinkedList<Task<U, I>>>();
    private HashMap<String, LinkedList<Task<U, I>>> failure                            = new LinkedHashMap<String, LinkedList<Task<U, I>>>();
    private HashMap<String, HashSet<Task<U, I>>>    assigned                           = new LinkedHashMap<String, HashSet<Task<U, I>>>();

    private HashMap<TKey<?, ?>, Object>             values                             = new HashMap<TKey<?, ?>, Object>();

    private final StorageEngine<I>                  storageEngine;
    private final LoggingEngine<I>                  loggingEngine;
    private final ResourceEngine                    resourceEngine;

    private final Execution<I>                      execution;
    private final Workflow<U, I>                    workflow;
    private final Properties                        properties;
    private final RevisableProgressMonitor          monitor;

    private boolean                                 paused;
    private boolean                                 initialized;
    private Throwable                               throwable;

    private int                                     scheduled                          = 0;
    private int                                     completed                          = 0;

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * @param workflow
     * @param storageEngine
     * @param loggingEngine
     * @param resourceEngine
     * @param properties
     * @param monitor
     */
    public UIMActiveExecution(Execution<I> execution, Workflow<U, I> workflow,
                              StorageEngine<I> storageEngine, LoggingEngine<I> loggingEngine,
                              ResourceEngine resourceEngine, Properties properties,
                              RevisableProgressMonitor monitor) {
        this.execution = execution;
        this.workflow = workflow;
        this.storageEngine = storageEngine;
        this.loggingEngine = loggingEngine;
        this.resourceEngine = resourceEngine;
        this.properties = properties;
        this.monitor = monitor;

        WorkflowStart<U, I> start = workflow.getStart();
        success.put(start.getIdentifier(), new LinkedList<Task<U, I>>());
        failure.put(start.getIdentifier(), new LinkedList<Task<U, I>>());
        assigned.put(start.getIdentifier(), new HashSet<Task<U, I>>());

        for (IngestionPlugin<U, I> step : workflow.getSteps()) {
            success.put(step.getIdentifier(), new LinkedList<Task<U, I>>());
            failure.put(step.getIdentifier(), new LinkedList<Task<U, I>>());
            assigned.put(step.getIdentifier(), new HashSet<Task<U, I>>());
        }
    }

    @Override
    public Execution<I> getExecution() {
        return execution;
    }

    @Override
    public StorageEngine<I> getStorageEngine() {
        return storageEngine;
    }

    @Override
    public LoggingEngine<I> getLoggingEngine() {
        return loggingEngine;
    }

    @Override
    public ResourceEngine getResourceEngine() {
        return resourceEngine;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public Workflow<U, I> getWorkflow() {
        return workflow;
    }

    @Override
    public UimDataSet<I> getDataSet() {
        return execution.getDataSet();
    }

    @Override
    public Collection<I> getDataSetCollection() {
        Collection<I> collection = null;
        UimDataSet<I> dataset = getDataSet();
        if (dataset instanceof Collection) {
            collection = (Collection<I>)dataset;
        } else if (dataset instanceof Request<?>) {
            collection = ((Request<I>)dataset).getCollection();
        } else if (dataset instanceof MetaDataRecord<?>) {
            collection = ((MetaDataRecord<I>)dataset).getCollection();
        }
        return collection;
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
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
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
    public Queue<Task<U, I>> getSuccess(String identifier) {
        return success.get(identifier);
    }

    @Override
    public Queue<Task<U, I>> getFailure(String identifier) {
        return failure.get(identifier);
    }

    @Override
    public Set<Task<U, I>> getAssigned(String identifier) {
        return assigned.get(identifier);
    }

    @Override
    public void incrementCompleted(int work) {
        completed += work;
    }

    @Override
    public int getProgressSize() {
        int size = 0;
        WorkflowStart<U, I> start = workflow.getStart();
        size += getProgressSize(start.getIdentifier());

        for (IngestionPlugin<U, I> step : workflow.getSteps()) {
            size += getProgressSize(step.getIdentifier());
        }
        return size;
    }

    private int getProgressSize(String name) {
        int size = 0;
        LinkedList<Task<U, I>> list = success.get(name);
        synchronized (list) {
            size += list.size();

            HashSet<Task<U, I>> set = assigned.get(name);
            size += set.size();
        }
        return size;
    }

    @Override
    public int getFailureSize() {
        // count elements in failure queues
        int size = 0;
        for (LinkedList<Task<U, I>> tasks : failure.values()) {
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

                WorkflowStart<U, I> start = workflow.getStart();
                int startSize = getProgressSize(start.getIdentifier());
                totalProgress += startSize;
                builder.append(start.getIdentifier());
                builder.append("=");
                builder.append(startSize);
                builder.append(", ");

                for (IngestionPlugin<U, I> step : workflow.getSteps()) {
                    int stepSize = getProgressSize(step.getIdentifier());
                    totalProgress += stepSize;
                    builder.append(step.getIdentifier());
                    builder.append("=");
                    builder.append(stepSize);
                    builder.append(", ");
                }

                log.info(scheduled + " scheduled, " + totalProgress + " in progress:" +
                         builder.toString());
            }
        }

        if (scheduled % 5000 == 0) {
            MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

            long mb = 1024 * 1024;
            log.info(String.format(
                    "Memory status: %d MB UsedHeap, %d MB MaxHeap, %d MB NonHeap, %d MB MaxNonHeap",
                    heapMemoryUsage.getUsed() / mb, heapMemoryUsage.getMax() / mb,
                    nonHeapMemoryUsage.getUsed() / mb, nonHeapMemoryUsage.getMax() / mb));
        }
    }

    @Override
    public int getCompletedSize() {
        return completed;
    }

    @Override
    public int getTotalSize() {
        return workflow.getStart().getTotalSize(this);
    }

    @Override
    public boolean isFinished() {
        if (getThrowable() != null) return true;
        if (!isInitialized()) return false;

        boolean cancelled = getMonitor().isCancelled();

        boolean finished = workflow.getStart().isFinished(this);

        boolean processed = getScheduledSize() == getFailureSize() + getCompletedSize();

        // we cannot guarantee this when something goes terribly wrong.
        // processed |= getThrowable() != null;

        boolean empty = getProgressSize() == 0;

// System.out.println(String.format("s=%d, p=%d, f=%d, c=%d, t=" + (getThrowable() != null ?
// getThrowable().getMessage() : ""), getScheduledSize(),
// getProgressSize(), getFailureSize(), getCompletedSize()));
        return (finished || cancelled) && processed && empty;
    }

    @Override
    public List<WorkflowStepStatus<U, I>> getStepStatus() {
        List<WorkflowStepStatus<U, I>> status = new ArrayList<WorkflowStepStatus<U, I>>();
        for (IngestionPlugin<U, I> step : workflow.getSteps()) {
            status.add(getStepStatus(step));
        }
        return status;
    }

    @Override
    public WorkflowStepStatus<U, I> getStepStatus(ExecutionPlugin<U, I> step) {
        Queue<Task<U, I>> success = getSuccess(step.getIdentifier());
        Queue<Task<U, I>> failure = getFailure(step.getIdentifier());

        int successSize = 0;
        synchronized (success) {
            successSize = success.size();
        }

        int failureSize = 0;
        Map<U, Throwable> exceptions = new HashMap<U, Throwable>();
        synchronized (failure) {
            failureSize = failure.size();
            for (Task<U, I> task : failure) {
                exceptions.put(task.getDataset(), task.getThrowable());
            }
        }

        WorkflowStepStatus<U, I> status = new UIMWorkflowStepStatus<U, I>(step, successSize,
                failureSize, exceptions);
        return status;
    }

// @Override
// public Workflow getWorkflow() {
// return workflow;
// }

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

// System.out.println("Finished:" + getCompletedSize());
// System.out.println("Failed:" + getFailureSize());
    }

    @Override
    public <NS, T extends Serializable> Object putValue(TKey<NS, T> key, T value) {
        return values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <NS, T extends Serializable> T getValue(TKey<NS, T> key) {
        Object object = values.get(key);
        if (object != null) { return (T)object; }
        return null;
    }

    @Override
    public File getWorkingDirectory(IngestionPlugin<U, I> plugin) {
        String workDirSuffix = workflow.getName() + File.separator + execution.getId() +
                               File.separator + plugin.getIdentifier();
        File workingDirectory = new File(resourceEngine.getWorkingDirectory(), workDirSuffix);
        if (!workingDirectory.exists() && !workingDirectory.mkdirs())
            throw new RuntimeException("Could not create working directory for this execution: " +
                                       workingDirectory.getAbsolutePath());
        if (!workingDirectory.isDirectory()) { throw new RuntimeException(
                "Path for working directory is not a directory: " +
                        workingDirectory.getAbsolutePath()); }
        return workingDirectory;
    }

    @Override
    public File getTmpDirectory(IngestionPlugin<U, I> plugin) {
        String tmpDirSuffix = workflow.getName() + File.separator + execution.getId() +
                              File.separator + plugin.getIdentifier();
        File tmpDirectory = new File(resourceEngine.getTemporaryDirectory(), tmpDirSuffix);
        if (!tmpDirectory.exists() && !tmpDirectory.mkdirs())
            throw new RuntimeException("Could not create temporary directory for this execution: " +
                                       tmpDirectory.getAbsolutePath());
        if (!tmpDirectory.isDirectory()) { throw new RuntimeException(
                "Path for temporary directory is not a directory: " +
                        tmpDirectory.getAbsolutePath()); }
        return tmpDirectory;
    }

    @Override
    public File getFileResource(String fileReference) {
        if (fileReference == null || fileReference.isEmpty()) { return null; }

        File resourceFile = new File(getResourceEngine().getResourceDirectory().getAbsolutePath() +
                                     File.separator + fileReference);
        return resourceFile;
    }

    @Override
    public synchronized void cleanup() {
// values.clear();
// values = null;

        if (!"true".equals(getProperties().getProperty(KEEP_TMP_FILES_AFTER_EXECUTION_KEY, "false"))) {
            if (resourceEngine != null) {
                String tmpExecutionDirSuffix = workflow.getName() + File.separator +
                                               execution.getId();
                File tmpExecutionDirectory = new File(resourceEngine.getTemporaryDirectory(),
                        tmpExecutionDirSuffix);
                if (tmpExecutionDirectory.isDirectory()) {
                    FileUtils.deleteQuietly(tmpExecutionDirectory);
                }
            }
        }
    }
}
