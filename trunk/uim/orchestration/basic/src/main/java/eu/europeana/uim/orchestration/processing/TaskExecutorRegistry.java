package eu.europeana.uim.orchestration.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;

/**
 * Registration for task executors to keep track of them implemented as singleton.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class TaskExecutorRegistry {
    private final static Logger           log       = Logger.getLogger(TaskExecutorRegistry.class.getName());

    private HashMap<String, TaskExecutor> executors = new LinkedHashMap<String, TaskExecutor>();

    private static TaskExecutorRegistry   instance;

    /**
     * @return single instance of this registry
     */
    public static TaskExecutorRegistry getInstance() {
        if (instance == null) {
            instance = new TaskExecutorRegistry();
        }
        return instance;
    }

    /**
     * Initializes a {@link TaskExecutor} and puts it on tracked executors.
     * 
     * @param name
     *            name of the new executor
     * @param coresize
     *            number of cores used
     * @param maxsize
     *            maximum size of cores
     */
    public void initialize(String name, int coresize, int maxsize) {
        TaskExecutor exec = executors.get(name);
        if (exec == null || exec.isShutdown()) {
            exec = new TaskExecutor(coresize, maxsize, new LinkedBlockingQueue<Runnable>(), name);
            exec.setRejectedExecutionHandler(new ReattachExecutionHandler());

            executors.put(name, exec);
        }
    }

    /** execute the given task on the named executor. This method
     * checks if the executor is still alive and if not the executor is 
     * restarted (recreated)
     * 
     * @param <I>
     * @param execution
     * @param name
     * @param task
     */
    public <I> void execute(ActiveExecution<I> execution, String name, Runnable task) {
        TaskExecutor executor = executors.get(name);
        try {
            executor.execute(task);
        } catch (IllegalThreadStateException t) {
            log.log(Level.SEVERE, "Thread pool <" + name + "> is for execution " +execution.getExecution().getId() + " is dead. Starting a new pool.");
            executor.shutdownNow();
            
            initialize(name, executor.getCorePoolSize(), executor.getMaximumPoolSize());
            executor = executors.get(name);
            executor.execute(task);
        }
    }

    /**
     * @return all registered executors
     */
    public Collection<TaskExecutor> getAllExecutor() {
        return executors.values();
    }

    /**
     * Shutdowns this registry with all the known executors.
     */
    public void shutdown() {
        for (TaskExecutor executor : executors.values()) {
            executor.shutdownNow();
        }
        executors.clear();
    }

    private final class ReattachExecutionHandler implements RejectedExecutionHandler {
        private HashMap<Runnable, Integer> retried = new HashMap<Runnable, Integer>();

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (executor.isShutdown() || executor.isTerminating() || executor.isTerminated()) {
                log.severe("Failed to schedule task (SHUTDOWN):" + r);
                return;
            }

            if (retried.containsKey(r)) {
                int count = retried.get(r);
                if (count < 50) {
                    retried.put(r, count + 1);

                    if (count % 5 == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                } else {
                    log.severe("Failed to schedule task:" + r);
                    return;
                }
            } else {
                retried.put(r, 1);
            }

            try {
                Thread.sleep(150);
                executor.execute(r);
            } catch (InterruptedException e) {
            }
        }
    }
}
