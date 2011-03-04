package eu.europeana.uim.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.orchestration.processing.TaskExecutor;
import eu.europeana.uim.orchestration.processing.TaskExecutorRegistry;
import eu.europeana.uim.orchestration.processing.TaskExecutorThread;
import eu.europeana.uim.orchestration.processing.TaskExecutorThreadFactory;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.TaskStatus;
import eu.europeana.uim.workflow.WorkflowStart;

public class UIMWorkflowProcessor implements Runnable {

    private static Logger                                             log        = Logger.getLogger(UIMWorkflowProcessor.class.getName());

    private TaskExecutorThreadFactory                                 factory    = new TaskExecutorThreadFactory(
                                                                                         "processor");
    private TaskExecutorThread                                        dispatcherThread;

    private final Registry                                            registry;

    private boolean                                                   running    = false;

    private static TKey<UIMWorkflowProcessor, ArrayList<TaskCreator>> SCHEDULED  = TKey.register(
                                                                                         UIMWorkflowProcessor.class,
                                                                                         "creators",
                                                                                         (Class<ArrayList<TaskCreator>>)new ArrayList<TaskCreator>().getClass());

    private List<ActiveExecution<Task>>                               executions = new ArrayList<ActiveExecution<Task>>();

    public UIMWorkflowProcessor(Registry registry) {
        this.registry = registry;
    }

    public void run() {
        running = true;
        while (running) {
            int total = 0;

            try {
                List<ActiveExecution<Task>> active = new ArrayList<ActiveExecution<Task>>();
                synchronized (executions) {
                    active.addAll(executions);
                }

                Iterator<ActiveExecution<Task>> activeIterator = active.iterator();
                while (activeIterator.hasNext()) {
                    ActiveExecution<Task> execution = activeIterator.next();
                    total += execution.getProgressSize();

                    // well we skip this execution if it is paused,
                    // FIXME: if only paused executions are around then
                    // we do somehow busy waiting - total count is > 0
                    if (execution.isPaused()) continue;

                    try {
                        // we ask the workflow start if we have more to do
                        WorkflowStart start = execution.getWorkflow().getStart();

                        if (execution.getProgressSize() == 0) {
                            processNoneInProgress(execution, start);
                        } else {
                            IngestionPlugin[] steps = execution.getWorkflow().getSteps().toArray(new IngestionPlugin[0]);
                            for (int i = steps.length - 1; i >= 0; i--) {
                                IngestionPlugin thisStep = steps[i];
                                IngestionPlugin prevStep = i > 0 ? steps[i - 1] : start;
                                
                                Queue<Task> prevSuccess = execution.getSuccess(prevStep.getClass().getSimpleName());
                                Queue<Task> prevFailure = execution.getFailure(prevStep.getClass().getSimpleName());
                                
                                Queue<Task> thisSuccess = execution.getSuccess(thisStep.getClass().getSimpleName());
                                Queue<Task> thisFailure = execution.getFailure(thisStep.getClass().getSimpleName());
                                
                                boolean savepoint = execution.getWorkflow().isSavepoint(thisStep);
                                
                                // if we are the "last" step we need to handle 
                                // the last success queue here.
                                if (i == steps.length - 1) {
                                    // save and clean final
                                    Task task = null;
                                    synchronized (thisSuccess) {
                                        task = thisSuccess.poll();
                                    }
                                    while (task != null) {
                                        execution.done(1);
                                        execution.getMonitor().worked(1);

                                        synchronized (thisSuccess) {
                                            task = thisSuccess.poll();
                                        }
                                    }
                                }
                                
                                // get successful tasks from previous step
                                // and schedule them into the step executor 
                                // of this step
                                TaskExecutor executor = TaskExecutorRegistry.getInstance().getExecutor(thisStep.getClass());
                                
                                Task task = null;
                                synchronized (prevSuccess) {
                                    task = prevSuccess.poll();
                                }

                                while (task != null) {

                                    task.setStep(thisStep);
                                    task.setSavepoint(savepoint);
                                    task.setOnSuccess(thisSuccess);
                                    task.setOnFailure(thisFailure);
                                    task.setStatus(TaskStatus.QUEUED);

                                    executor.execute(task);

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
                    } catch (Throwable exc) {
                        log.log(Level.WARNING, "Exception in workflow execution", exc);
                    }

                    if (total == 0) {
                        Thread.sleep(25);
                    }
                } // end synchronized execution
            } catch (Throwable exc) {
                log.log(Level.SEVERE, "Exception in workflow executor", exc);
            }

        }
    }

    private boolean processNoneInProgress(ActiveExecution<Task> execution, WorkflowStart start)
            throws StorageEngineException {
        Queue<Task> startTasks = execution.getSuccess(start.getClass().getSimpleName());

        // how many creators do we have
        ArrayList<TaskCreator> creators = execution.getValue(SCHEDULED);
        int inprogress = 0;
        Iterator<TaskCreator> creatorsIterator = creators.iterator();
        while (creatorsIterator.hasNext()) {
            TaskCreator next = creatorsIterator.next();
            if (next.isDone()) {
                creatorsIterator.remove();
            } else {
                inprogress++;
            }
        }

        if (!start.isFinished(execution, execution.getStorageEngine())) {
            if (inprogress < 5) {
                TaskCreator createLoader = start.createLoader(execution,
                        execution.getStorageEngine());
                if (createLoader != null) {
                    createLoader.setQueue(startTasks);
                    creators.add(createLoader);

                    TaskExecutorRegistry.getInstance().getExecutor(start.getClass()).execute(
                            createLoader);
                }
            }
        } else {
            if (inprogress == 0 && execution.isFinished()) {

                try {
                    start.completed(execution);
                } catch (Throwable t) {
                    log.log(Level.SEVERE, "Failed to complete:" + start, t);
                }
                for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
                    try {
                        step.completed(execution);
                    } catch (Throwable t) {
                        log.log(Level.SEVERE, "Failed to complete:" + step, t);
                    }
                }

                synchronized (execution) {
                    execution.setActive(false);
                    execution.setEndTime(new Date());
                    execution.getStorageEngine().updateExecution(execution.getExecution());
                }
                log.warning("Remove Execution:" + execution.toString());
                synchronized (executions) {
                    executions.remove(execution);
                }

                return false;
            }
        }

        return true;
    }

    public synchronized void schedule(ActiveExecution<Task> execution)
            throws StorageEngineException {
        if (execution.getWorkflow().getSteps().isEmpty())
            throw new IllegalStateException("Empty workflow not allowed: " +
                                            execution.getWorkflow().getClass().getName());

        WorkflowStart start = execution.getWorkflow().getStart();
        start.initialize(execution, execution.getStorageEngine());

        TaskExecutorRegistry.getInstance().initialize(start.getClass(),
                start.getMaximumThreadCount());

        HashSet<String> unique = new HashSet<String>();
        for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
            if (unique.contains(step.getName())) {
                throw new IllegalArgumentException("Workflow contains duplicate plugin:" +
                                                   step.getClass().getSimpleName());
            } else {
                unique.add(step.getName());
            }

            step.initialize(execution);
            TaskExecutorRegistry.getInstance().initialize(step.getClass(),
                    step.getMaximumThreadCount());
        }

        execution.putValue(SCHEDULED, new ArrayList<TaskCreator>());
        synchronized (executions) {
            executions.add(execution);
        }
    }

    public synchronized List<ActiveExecution<Task>> getExecutions() {
        return Collections.unmodifiableList(executions);
    }

    public void initialize() {
        running = false;
    }

    public void startup() {
        dispatcherThread = (TaskExecutorThread)factory.newThread(this);
        dispatcherThread.start();
    }

    public void shutdown() {
        running = false;
        dispatcherThread = null;
    }

    public void pause() {
        running = false;
        dispatcherThread = null;
    }

    public void resume() {
        running = true;
        dispatcherThread = (TaskExecutorThread)factory.newThread(this);
        dispatcherThread.start();
    }
}
