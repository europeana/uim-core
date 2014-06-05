package eu.europeana.uim.orchestration.basic;

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

import eu.europeana.uim.Registry;
import eu.europeana.uim.adapter.UimDatasetAdapter;
import eu.europeana.uim.common.SimpleThreadFactory;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.basic.processing.TaskExecutorRegistry;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.plugin.source.TaskStatus;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Processes a UIM workflow as a runnable.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UIMWorkflowProcessor<I> implements Runnable {
    private final static TKey<UIMWorkflowProcessor, ArrayList<TaskCreator>> SCHEDULED            = TKey.register(
                                                                                                         UIMWorkflowProcessor.class,
                                                                                                         "creators",
                                                                                                         (Class<ArrayList<TaskCreator>>)new ArrayList<TaskCreator>().getClass());

    private final static Logger                                             log                  = Logger.getLogger(UIMWorkflowProcessor.class.getName());

    private final SimpleThreadFactory                                       factory              = new SimpleThreadFactory(
                                                                                                         "processor");

    private final Registry                                                  registry;

    private Thread                                                          dispatcher;

    private boolean                                                         running              = false;

    private final List<ActiveExecution<?, I>>                               executions           = new ArrayList<ActiveExecution<?, I>>();
    private final List<ActiveExecution<?, I>>                               completions          = new ArrayList<ActiveExecution<?, I>>();

    // FIXME: Updated these values, cannot handle more
    private int                                                             maxRunningExecutions = 50;
    private int                                                             maxTotalProgress     = 500;
    private int                                                             maxInProgress        = 50;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public UIMWorkflowProcessor(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void run() {
        List<ActiveExecution<?, I>> active = new ArrayList<ActiveExecution<?, I>>();

        running = true;
        while (running) {
            boolean isbusy = false;
            int totalProgress = 0;

            active.clear();
            synchronized (executions) {
                active.addAll(executions);
            }

            for (ActiveExecution<?, I> execution : active) {
                totalProgress += execution.getProgressSize();
            }

            try {
                Iterator<ActiveExecution<?, I>> activeIterator = active.iterator();
                while (activeIterator.hasNext()) {
                    ActiveExecution<?, I> execution = activeIterator.next();

                    if (completeExecutionOnUnexpectedCause(execution)) {
                        continue;
                    }

                    // start working after the execution is initailized
                    if (execution.isInitialized()) {
                        boolean newtasks = false;
                        int execProgress = execution.getProgressSize();

                        if ((totalProgress < maxTotalProgress || active.size() * maxInProgress >= maxTotalProgress) && execProgress < maxInProgress) {
                            // we ask the work flow start if we have more to do
                            WorkflowStart start = execution.getWorkflow().getStart();
                            newtasks = ensureTasksInProgress(execution, start, execProgress,
                                    totalProgress);
                        }

                        // we need to wait until nothing is in progress before
                        // we acutally can cancel this execution (this way we
                        // never leave records in the pipe
                        if (execProgress == 0 && !newtasks) {
                            completeExecution(execution);
                        } else {
                            isbusy = executeSteps(execution, isbusy);
                        }
                    }
                }
            } catch (Throwable exc) {
                log.log(Level.SEVERE, "Exception in workflow execution", exc);
            }

            if (!isbusy) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // ignore interrupts
                }
            }

            if (executions.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // ignore interrupts
                }
            }
        }
    }

    private void completeExecution(ActiveExecution<?, I> execution) {
        // we ask the workflow start if we have more to do
        WorkflowStart start = execution.getWorkflow().getStart();
        ArrayList<TaskCreator> creators = execution.getValue(SCHEDULED);
        if (creators.isEmpty()) {
            if (start.isFinished(execution)) {
                // everything done no new
                if (execution.isFinished()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // ignore interrupts
                    }
                    if (execution.isFinished()) {
                        complete(execution, false);
                    }
                }
            }
        }
    }

    private boolean executeSteps(ActiveExecution<?, I> execution, boolean isbusy) {
        boolean isStillBusy = isbusy;

        // we ask the workflow start if we have more to do
        WorkflowStart start = execution.getWorkflow().getStart();

        IngestionPlugin[] steps = execution.getWorkflow().getSteps().toArray(new IngestionPlugin[0]);
        for (int i = steps.length - 1; i >= 0; i--) {
            // early failure, if something goes wrong we
            // can cancel the execution here "early"
            if (execution.getThrowable() != null) {
                complete(execution, true);
                break;
            }

            IngestionPlugin thisStep = steps[i];

            Queue prevSuccess = i > 0 ? execution.getSuccess(steps[i - 1].getIdentifier())
                    : execution.getSuccess(start.getIdentifier());

            Queue thisSuccess = execution.getSuccess(thisStep.getIdentifier());
            Queue thisFailure = execution.getFailure(thisStep.getIdentifier());
            Set thisAssigned = execution.getAssigned(thisStep.getIdentifier());

            boolean savepoint = execution.getWorkflow().isSavepoint(thisStep.getIdentifier());
            boolean mandatory = execution.getWorkflow().isMandatory(thisStep.getIdentifier());

            // if we are the "last" step we need to handle
            // the last success queue here.
            if (i == steps.length - 1) {
                finishTasksLastSuccess(execution, thisSuccess);
            }

            // get successful tasks from previous step
            // and schedule them into the step executor
            // of this step
            Task task = null;
            synchronized (prevSuccess) {
                task = (Task)prevSuccess.poll();
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

                isStillBusy |= true; // well there is something todo

                UimDatasetAdapter adapter = registry.getUimDatasetAdapter(thisStep.getIdentifier());
                task.setAdapter(adapter);

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
                    TaskExecutorRegistry.getInstance().execute(execution, thisStep.getIdentifier(),
                            task);
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
                    task = (Task)prevSuccess.poll();
                }
            }
        }

        return isStillBusy;
    }

    /**
     * @param execution
     *            execution under investigation
     * @return true, if execution has been completed on unexptected causes
     * @throws StorageEngineException
     */
    private boolean completeExecutionOnUnexpectedCause(ActiveExecution<?, I> execution) {
        boolean terminated = false;
        if (execution.isPaused()) {
            // did somebody cancel the paused job
            if (execution.getMonitor().isCancelled()) {
                complete(execution, true);
            }
            terminated = true;
        } else if (execution.getMonitor().isCancelled()) {
            complete(execution, true);
            terminated = true;
        } else if (execution.getThrowable() != null) {
            complete(execution, true);
            terminated = true;
        }
        return terminated;
    }

    private <U extends UimDataSet<I>> void finishTasksLastSuccess(ActiveExecution<U, I> execution,
            Queue<Task<U, I>> thisSuccess) {
        // save and clean final
        Task<U, I> task = null;
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

    private <U extends UimDataSet<I>> boolean ensureTasksInProgress(
            ActiveExecution<U, I> execution, WorkflowStart<U, I> start, int execProgress,
            int totalProgress) throws StorageEngineException {
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

        if (!start.isFinished(execution)) {
            if (activeCreators < 3) {
                log.fine("Less than 3 outstanding batches: <" + execution.getExecution().getId() +
                         ">  execution/total progress:" + execProgress + "/" + totalProgress);
                TaskCreator<U, I> createLoader = start.createLoader(execution);
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

    private <U extends UimDataSet<I>> void complete(ActiveExecution<U, I> execution, boolean cancel) {
        log.log(Level.INFO, "Remove Execution:" + execution.toString());
        synchronized (completions) {
            completions.add(execution);
        }
        removeExecution(execution);

        try {
            execution.getWorkflow().getStart().completed(execution);
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
            try {
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
            } catch (Throwable t) {
                log.log(Level.SEVERE, "Failed to update execution:" + execution, t);
            }
        }

        try {
            execution.getStorageEngine().checkpoint();
            execution.getLoggingEngine().completed(execution);
            execution.cleanup();
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to complete:" + execution, t);
        } finally {
            execution.getStorageEngine().completed(execution);
            if (registry.getLoggingEngine() != null) {
                registry.getLoggingEngine().log((Execution)execution.getExecution(), Level.INFO,
                        "UIMOrchestrator", "finish",
                        "Finished:" + execution.getExecution().getName());
            }
            synchronized (completions) {
                completions.remove(execution);
            }
        }
    }

    /**
     * Schedules the given execution.
     * 
     * @param execution
     * @throws StorageEngineException
     */
    public void schedule(final ActiveExecution<?, I> execution) throws StorageEngineException {
        if (execution.getWorkflow().getSteps().isEmpty()) { throw new IllegalStateException(
                "Empty workflow not allowed: " + execution.getWorkflow().getClass().getName()); }
        synchronized (executions) {
            int counter = 0;
            for (ActiveExecution<?, I> exec : executions) {
                if (!exec.isPaused()) {
                    counter++;
                }
            }
            executions.add(execution);
            if (counter >= maxRunningExecutions) {
                execution.setPaused(true);
            }
        }

        // init in separate thread, so that we are not blocking here.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WorkflowStart start = execution.getWorkflow().getStart();
                    start.initialize(execution);

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
                        removeExecution(execution);
                    }
                }
            }

        }, "Initializer" + execution.getExecution().getId() + ": " + execution.getWorkflow()).start();
    }

    private void removeExecution(final ActiveExecution<?, I> execution) {
        synchronized (executions) {
            executions.remove(execution);

            int counter = 0;
            for (ActiveExecution<?, I> exec : executions) {
                if (!exec.isPaused()) {
                    counter++;
                }
            }

            while (counter < maxRunningExecutions) {
                ActiveExecution<?, I> latest = null;
                for (ActiveExecution<?, I> exec : executions) {
                    if (exec.isPaused() &&
                        (latest == null || latest.getExecution().getStartTime().compareTo(
                                exec.getExecution().getStartTime()) < 0)) {
                        latest = exec;
                    }
                }
                if (latest == null) {
                    break;
                }
                latest.setPaused(false);
                counter++;
            }
        }
    }

    /**
     * @return scheduled executions
     */
    public synchronized List<ActiveExecution<?, I>> getExecutions() {
        ArrayList<ActiveExecution<?, I>> result = new ArrayList<ActiveExecution<?, I>>();
        synchronized (executions) {
            result.addAll(executions);
        }
        synchronized (completions) {
            result.addAll(completions);
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

    /**
     * @return maximum number of running executions at once
     */
    public int getMaxRunningExecutions() {
        return maxRunningExecutions;
    }

    /**
     * @param maxRunningExecutions
     *            maximum number of running executions at once
     */
    public void setMaxRunningExecutions(int maxRunningExecutions) {
        this.maxRunningExecutions = maxRunningExecutions;
    }
}
