package eu.europeana.uim.api;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * An Execution in a running state. It keeps track of the overall progress.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * 
 * @since Mar 21, 2011
 */
public interface ActiveExecution<I> extends Execution<I>, ExecutionContext {

    /**
     * @return storage engine for this execution
     */
    StorageEngine<I> getStorageEngine();
    
    /**
     * @return resource engine for this execution
     */

    ResourceEngine<I> getResourceEngine();
    
    /**
     * @param paused
     *            Should the execution be paused?
     */
    public void setPaused(boolean paused);

    /**
     * @return Is the execution be paused?
     */
    boolean isPaused();


    /**
     * @param initialized
     *            is the execution already initialized
     */
    public void setInitialized(boolean initialized);

    /**
     * @return Is the execution initialized
     */
    boolean isInitialized();

    /**
     * test the execution if all tasks are done either completely finished or failed. so if true:
     * scheduled == finished + failed
     * 
     * @return Is the execution finished?
     */
    boolean isFinished();

    /**
     * @param throwable
     *            holding heavy errors
     */
    void setThrowable(Throwable throwable);

    /**
     * @return throwable with errors
     */
    Throwable getThrowable();

    /**
     * @param name
     * @return success queue for the given plugin
     */
    Queue<Task> getSuccess(String name);

    /**
     * @param name
     * @return failures queue for the given plugin
     */
    Queue<Task> getFailure(String name);

    /**
     * @param name
     * @return set of tasks assigned to this execution
     */
    Set<Task> getAssigned(String name);

    /**
     * @param count
     */
    void incrementCompleted(int count);

    /**
     * gives an estimate of tasks/records which are currently in the pipeline. Note that failed
     * tasks are not counted. The system can not guarantee the number of records, due to the problem
     * that some of the tasks might change their status during the time of counting.
     * 
     * @return progress size
     */
    int getProgressSize();

    /**
     * gives the number of tasks/records which are completly finished successful by all steps.
     * 
     * @return amount of completed tasks
     */
    int getCompletedSize();

    /**
     * gives the number of tasks/records which have failed on the way through the workflow no matter
     * where.
     * 
     * @return amount of failures
     */
    int getFailureSize();

    /**
     * gives the number of tasks/records which have been scheduled to be processed in the first
     * place. So scheduled = progress + finished + failure.
     * 
     * @return amount of scheduled ones
     */
    int getScheduledSize();

    /**
     * gives the number of records which this execution will need to deal with. If not possible to
     * estimate Integer.MAX_VALUE is given.
     * 
     * @return amount of tasks
     */
    int getTotalSize();

    /**
     * @return status for all steps in the workflow
     */
    List<WorkflowStepStatus> getStepStatus();

    /**
     * @param step
     * @return status for the given plugin
     */
    WorkflowStepStatus getStepStatus(IngestionPlugin step);

    /**
     * Block until this execution is finished.
     */
    void waitUntilFinished();

    /**
     * @param work
     *            increment next work
     */
    void incrementScheduled(int work);
}
