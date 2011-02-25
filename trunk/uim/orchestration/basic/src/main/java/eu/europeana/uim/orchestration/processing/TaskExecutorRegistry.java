package eu.europeana.uim.orchestration.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class TaskExecutorRegistry {

	public final static Logger log = Logger.getLogger(TaskExecutorRegistry.class.getName());

	private HashMap<String, TaskExecutor> executor = new LinkedHashMap<String, TaskExecutor>();


	private static TaskExecutorRegistry instance;

	public static TaskExecutorRegistry getInstance() {
		if (instance == null) {
			instance = new TaskExecutorRegistry();
		}
		return instance;
	}


	public void initialize(Class clazz, int maxsize){
		if (!executor.containsKey(clazz.getSimpleName())){
			TaskExecutor exec = new TaskExecutor(1, maxsize, new LinkedBlockingQueue<Runnable>(), clazz.getSimpleName());
			exec.setRejectedExecutionHandler(new ReattachExecutionHandler());
			
			executor.put(clazz.getSimpleName(), exec);
		}
	}
	

	public TaskExecutor getExecutor(Class clazz){
		return executor.get(clazz.getSimpleName());
	}
	
	
	public Collection<TaskExecutor> getAllExecutor(){
		return executor.values();
	}

	
	

	private final class ReattachExecutionHandler implements RejectedExecutionHandler {
		private HashMap<Runnable, Integer> retried = new HashMap<Runnable, Integer>();

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (executor.isShutdown() || executor.isTerminating() || executor.isTerminated()){
				log.severe("Failed to schedule task (SHUTDOWN):" + r);
				return;
			}

			if (retried.containsKey(r)){
				int count = retried.get(r);
				if (count < 50){
					retried.put(r,  count + 1);

					if (count % 5 == 0){
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
