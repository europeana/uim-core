package eu.europeana.uim.orchestration.basic.processing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.common.SimpleThreadFactory;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskStatus;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * <p>
 * WorkerThreadPool is a ThreadGroup optimized for WorkerThreads, so the pool knows about its
 * specific threads - and vice versa.
 * </p>
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Nov 29, 2010
 */
public class TaskExecutor extends ThreadPoolExecutor {
    private static Logger log = Logger.getLogger(TaskExecutor.class.getName());

    /**
     * Constructor creates an worker thread pool of the specified size. The given scheduler is used
     * as task source.
     * 
     * @param corePoolSize
     * @param maxPoolSize
     * @param queue
     * @param name
     */
    public TaskExecutor(int corePoolSize, int maxPoolSize, BlockingQueue<Runnable> queue,
                        String name) {
        super(corePoolSize, maxPoolSize, 10, TimeUnit.SECONDS, queue, new SimpleThreadFactory(name,
                name));

        prestartCoreThread();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);

        if (r instanceof Task) {
            beforeExecuteTask((Task<?, ?>)r);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (r instanceof Task) {
            afterExecuteTask((Task<?, ?>)r, t);
        }
    }

    private <U extends UimDataSet<I>, I> void beforeExecuteTask(Task<U, I> task) {
        task.setUp();
        task.setStatus(TaskStatus.PROCESSING);
    }

    private <U extends UimDataSet<I>, I> void afterExecuteTask(Task<U, I> task, Throwable t) {
        boolean success = true;
        LoggingEngine<I> loggingEngine = task.getExecutionContext().getLoggingEngine();
        Execution<I> execution = task.getExecutionContext().getExecution();
        U metaDataRecord = task.getDataset();

        if (t != null) {
            success = false;
            if (t instanceof CorruptedDatasetException) {
                if (loggingEngine != null) {
                    loggingEngine.logFailed(execution, Level.WARNING, task.getStep(), t,
                            metaDataRecord, "Taskexecution",
                            "Major error in the workflow the metadata record is broken!");
                }
            } else if (t instanceof IngestionPluginFailedException) {
                if (loggingEngine != null) {
                    loggingEngine.logFailed(execution, Level.SEVERE, task.getStep(), t,
                            metaDataRecord, "PluginFailed",
                            "Major error in the workflow plugin execution must be stopped!");
                }
                task.getExecutionContext().setThrowable(t);

            } else {
                if (loggingEngine != null) {
                    loggingEngine.logFailed(execution, Level.WARNING, task.getStep(), t,
                            metaDataRecord, "Taskexception",
                            "An uncatched throwable occured:" + t.getMessage());
                }
            }
            log.log(java.util.logging.Level.SEVERE, "Task failed on record " + metaDataRecord +
                                                    " in plugin " + task.getStep().getIdentifier(),
                    t);
        } else if (!task.isSuccessfulProcessing()) {
            if (task.isMandatory()) {
                success = false;
                if (loggingEngine != null) {
                    loggingEngine.logFailed(
                            execution,
                            Level.WARNING,
                            task.getStep(),
                            t,
                            metaDataRecord,
                            "Taskexecution",
                            "Task could not perform its work and since it is mandatory for the workflow, the workflow cannot continue!");
                }
            }
        }

        if (success) {
            try {
                task.setStatus(TaskStatus.DONE);
                if (task.isSavepoint()) {
                    task.save();
                }

                synchronized (task.getOnSuccess()) {
                    task.getOnSuccess().add(task);

                    // within same synch block!!
                    synchronized (task.getAssigned()) {
                        task.getAssigned().remove(task);
                    }
                }

            } catch (StorageEngineException e1) {
                task.setThrowable(e1);
                task.setStatus(TaskStatus.FAILED);
                if (loggingEngine != null) {
                    loggingEngine.logFailed(execution, Level.SEVERE, task.getStep(), e1,
                            metaDataRecord, "StorageFailed",
                            "Major error in the storage execution must be stopped!");
                }

                synchronized (task.getOnFailure()) {
                    task.getOnFailure().add(task);
                    // within same synch block!!
                    synchronized (task.getAssigned()) {
                        task.getAssigned().remove(task);
                    }
                }

                task.getExecutionContext().setThrowable(
                        new IngestionPluginFailedException("Failed to save mdr.", e1));
            }
        } else {
            task.setThrowable(t);
            task.setStatus(TaskStatus.FAILED);
            synchronized (task.getOnFailure()) {
                task.getOnFailure().add(task);
                // within same synch block!!
                synchronized (task.getAssigned()) {
                    task.getAssigned().remove(task);
                }
            }

            try {
                task.save();
            } catch (Throwable e2) {
                if (loggingEngine != null) {
                    loggingEngine.logFailed(execution, Level.SEVERE, task.getStep(), e2,
                            metaDataRecord, "StorageFailed",
                            "Major error in the workflow plugin execution must be stopped!");
                }
                task.getExecutionContext().setThrowable(
                        new IngestionPluginFailedException("Failed to save mdr.", e2));
            }
        }

        task.tearDown();
    }
}
