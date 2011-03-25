package eu.europeana.uim.orchestration.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * Registration for task executors to keep track of them implemented as singleton.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
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

    /**
     * @param name
     * @return executor registered under the given name or null
     */
    public TaskExecutor getExecutor(String name) {
        return executors.get(name);
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
