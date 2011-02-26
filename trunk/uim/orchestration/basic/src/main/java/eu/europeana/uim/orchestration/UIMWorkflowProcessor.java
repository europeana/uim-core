package eu.europeana.uim.orchestration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.orchestration.processing.TaskExecutorRegistry;
import eu.europeana.uim.orchestration.processing.TaskExecutorThread;
import eu.europeana.uim.orchestration.processing.TaskExecutorThreadFactory;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;
import eu.europeana.uim.workflow.WorkflowStart;

public class UIMWorkflowProcessor implements Runnable {

    private static Logger               log        = Logger.getLogger(UIMWorkflowProcessor.class.getName());

    private TaskExecutorThreadFactory   factory    = new TaskExecutorThreadFactory("processor");
    private TaskExecutorThread          dispatcherThread;

    private final Registry              registry;

    private boolean                     running    = false;

    private List<ActiveExecution<Task>> executions = new ArrayList<ActiveExecution<Task>>();

    public UIMWorkflowProcessor(Registry registry) {
        this.registry = registry;
    }

    public void run() {
        running = true;
        while (running) {
            int total = 0;

            try {
                Iterator<ActiveExecution<Task>> iterator = executions.iterator();
                while (iterator.hasNext()) {
                    ActiveExecution<Task> execution = iterator.next();
                    total += execution.getProgressSize();

                    // well we skip this execution if it is paused,
                    // FIXME: if only paused executions are around then
                    // we do somehow busy waiting - total count is > 0
                    if (execution.isPaused()) continue;

                    try {
                        // we ask the workflow start if we have more to do
                        if (execution.getProgressSize() == 0) {
                            WorkflowStart start = execution.getWorkflow().getStart();
                            Task[] tasks = start.createWorkflowTasks(execution, execution.getStorageEngine());

                            if (tasks.length == 0) {
                                if (start.isFinished(execution, execution.getStorageEngine())) {
                                    Thread.sleep(100);
                                    if (execution.isFinished()) {
                                        Thread.sleep(100);

                                        if (execution.isFinished()) {
                                            execution.setActive(false);
                                            execution.setEndTime(new Date());

                                            start.completed(execution);
                                            for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
                                                step.completed(execution);
                                            }

                                            execution.getStorageEngine().updateExecution(
                                                    execution.getExecution());
                                            iterator.remove();
                                        }
                                    }
                                } else {
                                    Runnable loader = start.createLoader(execution, execution.getStorageEngine());
                                    if (loader != null) {
                                        TaskExecutorRegistry.getInstance().getExecutor(start.getClass()).execute(loader);
                                    }
                                }
                            } else {
                                Queue<Task> success = execution.getSuccess(start.getClass().getSimpleName());
                                success.addAll(Arrays.asList(tasks));
                                execution.incrementScheduled(tasks.length);
                            }
                        }

                        Queue<Task> success = execution.getSuccess(execution.getWorkflow().getStart().getClass().getSimpleName());

                        List<IngestionPlugin> steps = execution.getWorkflow().getSteps();
                        for (IngestionPlugin step : steps) {
                            boolean savepoint = execution.getWorkflow().isSavepoint(step);
                            Queue<Task> thisSuccess = execution.getSuccess(step.getClass().getSimpleName());
                            Queue<Task> thisFailure = execution.getFailure(step.getClass().getSimpleName());

                            // get successful tasks from previouse step
                            // and schedule them into the step executor.
                            Task task = null;
                            synchronized (success) {
                                task = success.poll();
                            }
                            while (task != null) {
                                task.setStep(step);
                                //TODO: should we make the last step always implicitly a savepoint
                                task.setSavepoint(savepoint);
                                task.setOnSuccess(thisSuccess);
                                task.setOnFailure(thisFailure);

                                task.setStatus(TaskStatus.QUEUED);
                                TaskExecutorRegistry.getInstance().getExecutor(step.getClass()).execute(task);

                                synchronized (success) {
                                    task = success.poll();
                                }
                            }

                            // make the current success list the "next" input list
                            success = thisSuccess;
                        }

                        // save and clean final
                        Task task;
                        synchronized (success) {
                            task = success.poll();
                        }
                        while (task != null) {
                            execution.done(1);
                            execution.getMonitor().worked(1);

                            synchronized (success) {
                                task = success.poll();
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

        TaskExecutorRegistry.getInstance().initialize(start.getClass(), start.getMaximumThreadCount());

        HashSet<String> unique = new HashSet<String>();
        for (IngestionPlugin step : execution.getWorkflow().getSteps()) {
            if (unique.contains(step.getClass().getSimpleName())) {
                throw new IllegalArgumentException("Workflow contains duplicate plugin:" + step.getClass().getSimpleName());
            } else {
                unique.add(step.getClass().getSimpleName());
            }

            step.initialize(execution);
            TaskExecutorRegistry.getInstance().initialize(step.getClass(), step.getMaximumThreadCount());
        }

        // start/execute the first loader task so that we do preload data
        Runnable loader = start.createLoader(execution, execution.getStorageEngine());
        if (loader != null) {
            TaskExecutorRegistry.getInstance().getExecutor(start.getClass()).execute(loader);
        }

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
