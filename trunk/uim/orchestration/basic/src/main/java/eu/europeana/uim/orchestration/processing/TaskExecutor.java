package eu.europeana.uim.orchestration.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;


/**
 * <p>WorkerThreadPool is a ThreadGroup optimized for 
 * WorkerThreads, so the pool knows about its specific
 * threads - and vice versa.</p>
 * 
 * @author ajuffing
 * @created Nov 29, 2004
 */
public class TaskExecutor extends ThreadPoolExecutor {
	private boolean isPaused;

	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();

	private LinkedList<Task> activeTask = new LinkedList<Task>();


	/** Constructor creates an worker thrad pool of the 
	 * specified size. The given scheduler is used as tasksource.
	 */
	public TaskExecutor(int corePoolSize, 
			int maxPoolSize,
			BlockingQueue<Runnable> queue,
			String name){
		super(corePoolSize,maxPoolSize,1, TimeUnit.SECONDS, queue, new TaskExecutorThreadFactory(name, name));

		prestartCoreThread();
	}


	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);

		pauseLock.lock();
		try {
			while (isPaused) unpaused.await();
		} catch(InterruptedException ie) {
			t.interrupt();
		} finally {
			pauseLock.unlock();
		}


		if (r instanceof Task){
			Task task = (Task)r;
			task.setUp();
			task.setStatus(TaskStatus.PROCESSING);

			synchronized(activeTask){
				activeTask.addLast(task);
			}
		}
	}  

	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);

		if (r instanceof Task){
			Task task = (Task)r;

			synchronized(activeTask){
				activeTask.remove(task);
			}

			if (t == null) {
				try {
					task.setStatus(TaskStatus.DONE);
					if (task.getStep().isSavepoint()) {
						task.save();
					}
					synchronized(task.getOnSuccess()) {
						task.getOnSuccess().add(task);
					}
				} catch (StorageEngineException e1) {
					task.setThrowable(t);
					task.setStatus(TaskStatus.FAILED);
					synchronized(task.getOnFailure()) {
						task.getOnFailure().add(task);
					}
					try {
						task.save();
					} catch (Throwable e2) {
						throw new RuntimeException("Failed to store failed record. Reason for failure:" + e1.getMessage(), e2);
					}
				}

			} else {
				task.setThrowable(t);
				task.setStatus(TaskStatus.FAILED);
				synchronized(task.getOnFailure()) {
					task.getOnFailure().add(task);
				}

				try {
					task.save();
				} catch (Throwable e2) {
					throw new RuntimeException("Failed to store failed record. Reason for failure:" + t.getMessage(), e2);
				}
			}
			task.tearDown();
		}
	}  


	public void pause(){
		pauseLock.lock();
		try {
			isPaused = true;
		} finally {
			pauseLock.unlock();
		}
	}


	public void resume(){
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}



	public List<Task> getTaskDump(){
		List<Task> result = new LinkedList<Task>();
		synchronized (activeTask){
			for (Task task : activeTask){
				result.add(task);
			}
		}
		return result;    
	}


	/**
	 */
	public int getAssigned() {
		return activeTask.size();
	}        


} 
