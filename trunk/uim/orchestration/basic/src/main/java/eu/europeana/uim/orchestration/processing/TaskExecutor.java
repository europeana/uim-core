package eu.europeana.uim.orchestration.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngine.Level;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;

/**
 * <p>
 * WorkerThreadPool is a ThreadGroup optimized for WorkerThreads, so the pool knows about its
 * specific threads - and vice versa.
 * </p>
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Nov 29, 2010
 */
public class TaskExecutor extends ThreadPoolExecutor {
    private boolean          isPaused;

    private ReentrantLock    pauseLock  = new ReentrantLock();
    private Condition        unpaused   = pauseLock.newCondition();

    private LinkedList<Task> activeTask = new LinkedList<Task>();

    /**
     * Constructor creates an worker thrad pool of the specified size. The given scheduler is used
     * as tasksource.
     * 
     * @param corePoolSize
     * @param maxPoolSize
     * @param queue
     * @param name
     */
    public TaskExecutor(int corePoolSize, int maxPoolSize, BlockingQueue<Runnable> queue,
                        String name) {
        super(corePoolSize, maxPoolSize, 1, TimeUnit.SECONDS, queue, new TaskExecutorThreadFactory(
                name, name));

        prestartCoreThread();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);

        pauseLock.lock();
        try {
            while (isPaused)
                unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }

        if (r instanceof Task) {
            Task task = (Task)r;
            task.setUp();
            task.setStatus(TaskStatus.PROCESSING);

            synchronized (activeTask) {
                activeTask.addLast(task);
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (r instanceof Task) {
            Task task = (Task)r;

            boolean success = true;
            LoggingEngine<?> loggingEngine = task.getExecutionContext().getLoggingEngine();
            if (t != null) {
                success = false;
                if (loggingEngine != null &&
                    t.getClass().equals(CorruptedMetadataRecordException.class)) {
                    loggingEngine.log(Level.WARNING,
                            "Major error in the workflow the metadata record is broken!",
                            task.getExecutionContext().getExecution(), task.getMetaDataRecord(),
                            task.getStep());
                }
            } else if (!task.isSuccessfulProcessing()) {
                if (task.isMandatory()) {
                    success = false;
                    if (loggingEngine != null) {
                        loggingEngine.log(
                                Level.WARNING,
                                "Task could not perform its work and since it is mandatory for the workflow, the workflow cannot continue!",
                                task.getExecutionContext().getExecution(),
                                task.getMetaDataRecord(), task.getStep());
                    }
                } else {
                    if (loggingEngine != null) {
                        loggingEngine.log(
                                Level.WARNING,
                                "Task could not perform its work, but the processing of the meta data record can continue!",
                                task.getExecutionContext().getExecution(),
                                task.getMetaDataRecord(), task.getStep());
                    }
                }
            }

            synchronized (activeTask) {
                activeTask.remove(task);
            }

            if (success) {
                try {
                    task.setStatus(TaskStatus.DONE);
                    if (task.isSavepoint()) {
                        task.save();
                    }
                    synchronized (task.getOnSuccess()) {
                        task.getOnSuccess().add(task);
                    }
                } catch (StorageEngineException e1) {
                    task.setThrowable(t);
                    task.setStatus(TaskStatus.FAILED);
                    synchronized (task.getOnFailure()) {
                        task.getOnFailure().add(task);
                    }
                    try {
                        task.save();
                    } catch (Throwable e2) {
                        throw new RuntimeException(
                                "Failed to store failed record. Reason for failure:" +
                                        e1.getMessage(), e2);
                    }
                }
            } else {
                task.setThrowable(t);
                task.setStatus(TaskStatus.FAILED);
                synchronized (task.getOnFailure()) {
                    task.getOnFailure().add(task);
                }

                try {
                    task.save();
                } catch (Throwable e2) {
                    throw new RuntimeException(
                            "Failed to store failed record. Reason for failure:" + (t != null ? t.getMessage() : ""),
                            e2);
                }
            }
            task.tearDown();
        }
    }

    /**
     * Pauses execution.
     */
    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * Resumes execution
     */
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * @return task dumb (what are active tasks)
     */
    public List<Task> getTaskDump() {
        List<Task> result = new LinkedList<Task>();
        synchronized (activeTask) {
            for (Task task : activeTask) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * @return number of active tasks
     */
    public int getAssigned() {
        return activeTask.size();
    }
}
