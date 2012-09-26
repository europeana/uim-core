package eu.europeana.uim.common;

/** IProgressMonitor motivated interface for progress monitoring.
 * 
 * @author andreas.juffinger@kb.nl
 */
public interface ProgressMonitor {

	/** Method to setup the progress monitor with necessary information. The name 
	 * of the overall task, and the number of expected work units
	 * 
	 * @param task the name of the task
	 * @param work the expected number of work units
	 */
	public void beginTask(String task, int work);

	
	/** Inform the monitor about a number of work units done.
	 * 
	 * @param work
	 */
	public void worked(int work);
	
	/** Inform the monitor that the task is finished 
	 * 
	 */
	public void done();

	
	/** Inform the monitor of the name of the sub task we
	 * are currently working on.
	 * 
	 * @param subtask
	 */
	public void subTask(String subtask);
	

	/** Set the internal cancel value of the progress monitor. This can be set
	 * by the user interface therefore the execution should poll the <code>isCancelled</code>
	 * method before every sub task.
	 * 
	 * @param cancelled
	 */
	public void setCancelled(boolean cancelled);

	
	/** Check whether the task execution has been canceled or not.
	 * 
	 * @return whether the execution should be cancelled.
	 */
	public boolean isCancelled();
	
}
