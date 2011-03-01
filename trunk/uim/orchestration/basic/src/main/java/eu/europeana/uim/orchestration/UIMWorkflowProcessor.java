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
                Iterator<ActiveExecution<Task>> executionIterator = executions.iterator();
                while (executionIterator.hasNext()) {
                    ActiveExecution<Task> execution = executionIterator.next();
                    total += execution.getProgressSize();

                    // well we skip this execution if it is paused,
                    // FIXME: if only paused executions are around then
                    // we do somehow busy waiting - total count is > 0
                    if (execution.isPaused()) continue;

                    try {
                        // we ask the workflow start if we have more to do
                        WorkflowStart start = execution.getWorkflow().getStart();
                        Queue<Task> startTasks = execution.getSuccess(start.getClass().getSimpleName());

                        if (execution.getProgressSize() == 0) {

                            if (!start.isFinished(execution, execution.getStorageEngine())) {
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

                                if (creators.size() < 5) {
                                    TaskCreator createLoader = start.createLoader(execution,
                                            execution.getStorageEngine());
                                    if (createLoader != null) {
                                        createLoader.setQueue(startTasks);
                                        creators.add(createLoader);

                                        TaskExecutorRegistry.getInstance().getExecutor(
                                                start.getClass()).execute(createLoader);
                                    }
                                }
                            } else {
                                ArrayList<TaskCreator> creators = execution.getValue(SCHEDULED);
                                if (creators.isEmpty() && execution.isFinished()) {
                                    execution.setActive(false);
                                    execution.setEndTime(new Date());

                                    start.completed(execution);
                                    for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
                                        step.completed(execution);
                                    }

                                    execution.getStorageEngine().updateExecution(
                                            execution.getExecution());
                                    
                                    log.warning("Remove Execution:" + execution.toString());
                                    executionIterator.remove();
                                }
                            }
                        }

                        boolean firststep = true;

                        Queue<Task> current = startTasks;

                        List<IngestionPlugin> steps = execution.getWorkflow().getSteps();
                        for (IngestionPlugin step : steps) {

                            boolean savepoint = execution.getWorkflow().isSavepoint(step);
                            Queue<Task> thisSuccess = execution.getSuccess(step.getClass().getSimpleName());
                            Queue<Task> thisFailure = execution.getFailure(step.getClass().getSimpleName());

                            // System.out.println("Step size:" + thisSuccess.size());
                            // get successful tasks from previouse step
                            // and schedule them into the step executor.
                            Task task = null;
                            synchronized (current) {
                                //if (!current.isEmpty()) {
                                    task = current.poll();
                                //}
                            }

                            while (task != null) {

                                task.setStep(step);
                                // TODO: should we make the last step always implicitly a savepoint
                                task.setSavepoint(savepoint);
                                task.setOnSuccess(thisSuccess);
                                task.setOnFailure(thisFailure);

                                task.setStatus(TaskStatus.QUEUED);

                                TaskExecutorRegistry.getInstance().getExecutor(step.getClass()).execute(
                                        task);

                                if (firststep) {
                                    execution.incrementScheduled(1);
                                }

                                synchronized (current) {
                                    //if (!current.isEmpty()) {
                                        task = current.poll();
                                    //}
                                }
                            }

                            // make the current success list the "next" input list
                            current = thisSuccess;
                            firststep = false;
                        }

                        // save and clean final
                        Task task = null;
                        synchronized (current) {
                            //if (!current.isEmpty()) {
                                task = current.poll();
                            //}
                        }
                        while (task != null) {
                            execution.done(1);
                            execution.getMonitor().worked(1);

                            synchronized (current) {
                                //if (!current.isEmpty()) {
                                    task = current.poll();
                                //}
                            }
                        }

                    } catch (Throwable exc) {
                        log.log(Level.WARNING, "Exception in workflow execution", exc);
                    }
                }

                if (total == 0) {
                    Thread.sleep(25);
                }
            } catch (Throwable exc) {
                log.log(Level.SEVERE, "Exception in workflow executor", exc);
            }
        }
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
        executions.add(execution);
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
