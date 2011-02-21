package eu.europeana.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
 * 
 * @author andreas.juffinger@kb.nl
 */
public class Progress implements IsSerializable {

	private String task ="Not defined";
	private String subtask ="Not defined";
	
	private int work =0;
	private int worked =0;
	
	private boolean done = false;

	/** public standard constructor.
	 */
	public Progress() {
	}


	public String getTask() {
		return task;
	}


	public void setTask(String task) {
		this.task = task;
	}


	public String getSubtask() {
		return subtask;
	}


	public void setSubtask(String subtask) {
		this.subtask = subtask;
	}


	public int getWork() {
		return work;
	}


	public void setWork(int work) {
		this.work = work;
	}


	public int getWorked() {
		return worked;
	}


	public void setWorked(int worked) {
		this.worked = worked;
	}


	public boolean isDone() {
		return done;
	}


	public void setDone(boolean done) {
		this.done = done;
	}

	

}
