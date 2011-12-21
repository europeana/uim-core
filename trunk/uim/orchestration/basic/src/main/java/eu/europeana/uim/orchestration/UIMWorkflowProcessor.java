package eu.europeana.uim.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.SimpleThreadFactory;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.processing.TaskExecutorRegistry;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.TaskStatus;
import eu.europeana.uim.workflow.WorkflowStart;

/**
 * Processes a UIM workflow as a runnable.
 * 
 * @param <I>
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class UIMWorkflowProcessor<I> implements Runnable {
    private static Logger                                             log              = Logger.getLogger(UIMWorkflowProcessor.class.getName());

    private SimpleThreadFactory                                       factory          = new SimpleThreadFactory(
                                                                                               "processor");
    private Thread                                                    dispatcher;

    private final Registry                                            registry;

    private boolean                                                   running          = false;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static TKey<UIMWorkflowProcessor, ArrayList<TaskCreator>> SCHEDULED        = TKey.register(
                                                                                               UIMWorkflowProcessor.class,
                                                                                               "creators",
                                                                                               (Class<ArrayList<TaskCreator>>)new ArrayList<TaskCreator>().getClass());

    private List<ActiveExecution<I>>                                  executions       = new ArrayList<ActiveExecution<I>>();

    private int                                                       maxTotalProgress = 5000;

    private int                                                       maxInProgress    = 1000;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public UIMWorkflowProcessor(Registry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        List<ActiveExecution<I>> active = new ArrayList<ActiveExecution<I>>();

        running = true;
        while (running) {
            boolean isbusy = false;
            int totalProgress = 0;

            active.clear();
            synchronized (executions) {
                active.addAll(executions);
            }

            for (ActiveExecution<?> execution : active) {
                totalProgress += execution.getProgressSize();
            }

            try {

                Iterator<ActiveExecution<I>> activeIterator = active.iterator();
                while (activeIterator.hasNext()) {
                    ActiveExecution<I> execution = activeIterator.next();
                    if (execution.isPaused()) {
                        // did somebody cancel the paused job
                        if (execution.getMonitor().isCancelled()) {
                            complete(execution, true);
                        }
                        continue;
                    } else if (execution.getMonitor().isCancelled()) {
                        complete(execution, true);
                        continue;
                    } else if (execution.getThrowable() != null) {
                        complete(execution, true);
                        continue;
                    }

                    try {
                        // start working after the execution is initailized
                        if (execution.isInitialized()) {
                            // we ask the workflow start if we have more to do
                            WorkflowStart start = execution.getWorkflow().getStart();
                            boolean newtasks = false;
                            int execProgress = execution.getProgressSize();

                            if (totalProgress <= maxTotalProgress && execProgress <= maxInProgress) {
                                newtasks = ensureTasksInProgress(execution, start, execProgress,
                                        totalProgress);
                            }

                            // we need to wait until nothing is in progress before
                            // we acutally can cancel this execution (this way we
                            // never leave records in the pipe
                            if (execProgress == 0 && !newtasks) {
                                ArrayList<TaskCreator> creators = execution.getValue(SCHEDULED);
                                if (creators.isEmpty()) {
                                    if (start.isFinished(execution, execution.getStorageEngine())) {
                                        // everything done no new
                                        if (execution.isFinished()) {
                                            Thread.sleep(100);
                                            if (execution.isFinished()) {
                                                complete(execution, false);
                                            }
                                        }
                                    }
                                }
                            } else {
                                IngestionPlugin[] steps = execution.getWorkflow().getSteps().toArray(
                                        new IngestionPlugin[0]);
                                for (int i = steps.length - 1; i >= 0; i--) {
                                    // early failure, if something goes wrong we
                                    // can cancel the execution here "early"
                                    if (execution.getThrowable() != null) {
                                        complete(execution, true);
                                        break;
                                    }

                                    IngestionPlugin thisStep = steps[i];

                                    Queue<Task<I>> prevSuccess = i > 0
                                            ? execution.getSuccess(steps[i - 1].getIdentifier())
                                            : execution.getSuccess(start.getIdentifier());

                                    Queue<Task<I>> thisSuccess = execution.getSuccess(thisStep.getIdentifier());
                                    Queue<Task<I>> thisFailure = execution.getFailure(thisStep.getIdentifier());
                                    Set<Task<I>> thisAssigned = execution.getAssigned(thisStep.getIdentifier());

                                    boolean savepoint = execution.getWorkflow().isSavepoint(
                                            thisStep.getIdentifier());
                                    boolean mandatory = execution.getWorkflow().isMandatory(
                                            thisStep.getIdentifier());

                                    // if we are the "last" step we need to handle
                                    // the last success queue here.
                                    if (i == steps.length - 1) {
                                        finishTasksLastSuccess(execution, thisSuccess);
                                    }

                                    // get successful tasks from previous step
                                    // and schedule them into the step executor
                                    // of this step
                                    Task<I> task = null;
                                    synchronized (prevSuccess) {
                                        task = prevSuccess.poll();
                                    }

                                    while (task != null) {
                                        // early failure, if something goes wrong we
                                        // can cancel the execution here "early"
                                        // we cannot check the task, because we do not
                                        // know when it's going to be executed
                                        if (execution.getThrowable() != null) {
                                            complete(execution, true);
                                            break;
                                        }

                                        isbusy |= true; // well there is something todo

                                        task.setStep(thisStep, mandatory);
                                        task.setSavepoint(savepoint);
                                        task.setOnSuccess(thisSuccess);
                                        task.setOnFailure(thisFailure);
                                        task.setAssigned(thisAssigned);
                                        // mandatory
                                        task.setStatus(TaskStatus.QUEUED);

                                        synchronized (thisAssigned) {
                                            thisAssigned.add(task);
                                        }

                                        try {
                                            TaskExecutorRegistry.getInstance().execute(execution,
                                                    thisStep.getIdentifier(), task);
                                        } catch (Throwable t) {
                                            // if something goes wrong here
                                            // we have a serios problem and
                                            // should not continue the execution.
                                            execution.setThrowable(t);
                                            complete(execution, true);
                                        }

                                        // if this is the first step,
                                        // then we have just now scheduled a
                                        // newly created task from the start plugin.
                                        if (i == 0) {
                                            execution.incrementScheduled(1);
                                        }

                                        synchronized (prevSuccess) {
                                            task = prevSuccess.poll();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Throwable exc) {
                        log.log(Level.WARNING, "Exception in workflow execution", exc);
                    }
                } // end synchronized execution
            } catch (Throwable exc) {
                log.log(Level.SEVERE, "Exception in workflow executor", exc);
            }

            if (!isbusy) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }

            if (executions.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void finishTasksLastSuccess(ActiveExecution<I> execution, Queue<Task<I>> thisSuccess) {
        // save and clean final
        Task<I> task = null;
        synchronized (thisSuccess) {
            task = thisSuccess.poll();
        }
        while (task != null) {
            execution.incrementCompleted(1);
            execution.getMonitor().worked(1);

            synchronized (thisSuccess) {
                task = thisSuccess.poll();
            }
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private boolean ensureTasksInProgress(ActiveExecution<I> execution, WorkflowStart start,
            int execProgress, int totalProgress) throws StorageEngineException {
        // how many creators do we have
        ArrayList<TaskCreator> creators = execution.getValue(SCHEDULED);

        // number of scheduled but not done creation tasks.
        int activeCreators = 0;
        Iterator<TaskCreator> creatorsIterator = creators.iterator();
        while (creatorsIterator.hasNext()) {
            TaskCreator next = creatorsIterator.next();
            if (next.isDone()) {
                creatorsIterator.remove();
            } else {
                activeCreators++;
            }
        }

        if (!start.isFinished(execution, execution.getStorageEngine())) {
            if (activeCreators < 3) {
                log.fine("Less than 3 outstanding batches: <" + execution.getExecution().getId() +
                         ">  execution/total progress:" + execProgress + "/" + totalProgress);
                TaskCreator<I> createLoader = start.createLoader(execution,
                        execution.getStorageEngine());
                if (createLoader != null) {
                    createLoader.setQueue(execution.getSuccess(start.getIdentifier()));
                    creators.add(createLoader);

                    TaskExecutorRegistry.getInstance().execute(execution, start.getIdentifier(),
                            createLoader);
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void complete(ActiveExecution execution, boolean cancel) throws StorageEngineException {
        try {
            execution.getWorkflow().getStart().completed(execution, execution.getStorageEngine());
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to complete:" + execution.getWorkflow().getStart(), t);
        }
        for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
            try {
                step.completed(execution);
                // FIXME: what to do if we are cancelled and
                // records are in the complete output pipe?
            } catch (Throwable t) {
                log.log(Level.SEVERE, "Failed to complete:" + step, t);
            }
        }

        synchronized (execution) {
            Execution executionBean = execution.getExecution();

            executionBean.setActive(false);
            executionBean.setEndTime(new Date());
            if (cancel) {
                executionBean.setCanceled(true);
            } else {
                executionBean.setCanceled(false);
            }

            executionBean.setSuccessCount(execution.getCompletedSize());
            executionBean.setFailureCount(execution.getFailureSize());
            executionBean.setProcessedCount(execution.getScheduledSize());

            execution.getStorageEngine().updateExecution(executionBean);
        }

        try {
            execution.getStorageEngine().checkpoint();
            execution.cleanup();
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to complete:" + execution, t);
        } finally {
            execution.getStorageEngine().completed(execution);
            if (registry.getLoggingEngine() != null) {
                registry.getLoggingEngine().log(execution.getExecution(), Level.INFO,
                        "UIMOrchestrator", "finish",
                        "Finished:" + execution.getExecution().getName());
            }

            log.warning("Remove Execution:" + execution.toString());
            synchronized (executions) {
                executions.remove(execution);
            }
        }

    }

    /**
     * Schedules the given execution.
     * 
     * @param execution
     * @throws StorageEngineException
     */
    public void schedule(final ActiveExecution<I> execution) throws StorageEngineException {
        if (execution.getWorkflow().getSteps().isEmpty()) { throw new IllegalStateException(
                "Empty workflow not allowed: " + execution.getWorkflow().getClass().getName()); }
        synchronized (executions) {
            executions.add(execution);
        }

        // init in separate thread, so that we are not blocking here.
        new Thread(new Runnable() {
            @SuppressWarnings({ "rawtypes" })
            @Override
            public void run() {
                try {
                    WorkflowStart start = execution.getWorkflow().getStart();
                    start.initialize(execution, execution.getStorageEngine());

                    TaskExecutorRegistry.getInstance().initialize(start.getIdentifier(),
                            start.getPreferredThreadCount(), start.getMaximumThreadCount());

                    HashSet<String> unique = new HashSet<String>();
                    for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
                        if (unique.contains(step.getIdentifier())) {
                            throw new IllegalArgumentException(
                                    "Workflow contains duplicate plugin:" +
                                            step.getClass().getSimpleName());
                        } else {
                            unique.add(step.getIdentifier());
                        }

                        step.initialize(execution);
                        TaskExecutorRegistry.getInstance().initialize(step.getIdentifier(),
                                step.getPreferredThreadCount(), step.getMaximumThreadCount());
                    }

                    execution.putValue(SCHEDULED, new ArrayList<TaskCreator>());
                    execution.setInitialized(true);

                } catch (Throwable t) {
                    log.log(Level.SEVERE, "Failed to startup execution.", t);
                    try {
                        execution.setThrowable(t);
                        execution.getExecution().setCanceled(true);
                        execution.getExecution().setActive(false);
                        execution.getExecution().setEndTime(new Date());
                        execution.getStorageEngine().updateExecution(execution.getExecution());
                    } catch (StorageEngineException e) {
                        log.log(Level.SEVERE, "Failed to persist failed execution.", e);
                    } finally {
                        synchronized (executions) {
                            executions.remove(execution);
                        }
                    }
                }
            }
        }, "Initializer" + execution.getExecution().getId() + ": " + execution.getWorkflow()).start();
    }

    /**
     * @return scheduled executions
     */
    public synchronized List<ActiveExecution<I>> getExecutions() {
        ArrayList<ActiveExecution<I>> result = new ArrayList<ActiveExecution<I>>();
        synchronized (executions) {
            result.addAll(executions);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Initializes the processor, right now only switch running flag.
     */
    public void initialize() {
        running = false;
    }

    /**
     * Starts up the thread for dispatching.
     */
    public void startup() {
        dispatcher = factory.newThread(this);
        dispatcher.start();
    }

    /**
     * Shuts down the dispatching and stops running.
     */
    public void shutdown() {
        running = false;
        dispatcher = null;
    }

    /**
     * Halt temporarily the execution.
     */
    public void pause() {
        running = false;
        dispatcher = null;
    }

    /**
     * Continue running.
     */
    public void resume() {
        running = true;
        dispatcher = factory.newThread(this);
        dispatcher.start();
    }

    /**
     * @return number of processes in progress
     */
    public int getMaxTotalProgress() {
        return maxTotalProgress;
    }

    /**
     * @param maxTotalProgress
     *            number of processes in progress
     */
    public void setMaxTotalProgress(int maxTotalProgress) {
        this.maxTotalProgress = maxTotalProgress;
    }

    /**
     * @param maxInProgress
     *            maximum number of processes in progress
     */
    public void setMaxInProgress(int maxInProgress) {
        this.maxInProgress = maxInProgress;
    }

    /**
     * @return maximum number of processes in progress
     */
    public int getMaxInProgress() {
        return maxInProgress;
    }
}
