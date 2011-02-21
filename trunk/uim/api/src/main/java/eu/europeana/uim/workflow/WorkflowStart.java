package eu.europeana.uim.workflow;

import eu.europeana.uim.api.ActiveExecution;




/**
 * Start in a UIM workflow. We use this in order to implement the command 
 * pattern for workflow execution. The start step is an emphasized step which has 
 * additional functionality.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStart extends WorkflowStep{

	/** Create a runnable which is executed within a thread pool. The
	 * runnable is then scheduled and executed in another thread
	 * 
	 * @param <T>
	 * @param execution
	 * @return runnable which runs in a thread pool executor and loads data
	 */
	<T> Runnable createLoader(ActiveExecution<T>  execution);
	
    /** Create the tasks  (@see {@link Task}) which are then processed through
     * the workflow and passed on from step to step.
     * 
     * @param <T>
     * @param execution
     * @return number of tasks created.
     */
    <T> int createWorkflowTasks(ActiveExecution<T>  execution);

	/** Check wheater there is more work to do or not. Finished means,
	 * that no new tasks can be created.
	 * 
	 * @param <T>
	 * @param execution
	 * @return true iff no more tasks can be created.
	 */
	<T> boolean isFinished(ActiveExecution<T> execution);

	/** Get the number of remaining records for this execution
	 * @param execution 
	 * @param <T> 
	 * 
	 * @return an estimate on the number of records for which no tasks 
	 * have been created yet
	 */
	//<T> int getRemaining(ActiveExecution<T> execution);
    
}
