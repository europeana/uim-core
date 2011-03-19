package eu.europeana.uim.api;

import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * An Execution in a running state. It keeps track of the overall progress.
 */
public interface ActiveExecution<T> extends Execution, ExecutionContext {

	StorageEngine getStorageEngine();

    public void setPaused(boolean paused);
    boolean isPaused();

    /** test the execution if all tasks are done eather completly finished
     * or failed. so if true: scheduled == finished + failed
     * 
     * @return
     */
    boolean isFinished();

	void setThrowable(Throwable throwable);
	Throwable getThrowable();


	Queue<T> getSuccess(String name);
	Queue<T> getFailure(String name);
	Set<Task> getAssigned(String name);

	void incrementCompleted(int count);
	
	
	/** gives an estimate of tasks/records which are currently in the pipeline.
	 * Note that failed tasks are not counted. The system can not guarantee the 
	 * number of records, due to the problem that some of the tasks might change 
	 * their status during the time of counting.
	 * 
	 * @return
	 */
	int getProgressSize();
	
	/** gives the number of tasks/records which are completly finished successful
	 * by all steps.
	 * 
	 * @return
	 */
	int getCompletedSize();

	/** gives the number of tasks/records which have failed on the way through
	 * the workflow no matter where.
	 * 
	 * @return
	 */
	int getFailureSize();

	/** gives the number of tasks/records which have been scheduled to be processed
	 * in the first place. So scheduled = progress + finished + failure.
	 * 
	 * @return
	 */
	int getScheduledSize();
	
	
	/** gives the number of records which this execution will need to deal with. If
	 * not possible to estimate Integer.MAX_VALUE is given.
	 * 
	 * @return
	 */
	int getTotalSize();
	
	List<WorkflowStepStatus> getStepStatus();
	WorkflowStepStatus getStepStatus(IngestionPlugin step);

	public Properties getProperties();

	void waitUntilFinished();
	

	/**
	 * @param work
	 */
	void incrementScheduled(int work);
	
}
