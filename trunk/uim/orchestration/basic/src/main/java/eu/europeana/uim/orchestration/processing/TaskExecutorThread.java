package eu.europeana.uim.orchestration.processing;

import java.util.Hashtable;

public class TaskExecutorThread extends Thread {
	  
	  protected ThreadGroup group;


	  protected Hashtable<String ,Object> threadContext = new Hashtable<String ,Object>();
	  
	  public TaskExecutorThread(ThreadGroup group, Runnable runable, String name){
	    super(group,runable, name);
	    this.group = group;
	  }
	  
	  
	  /**
	   * @return Returns the threadContext.
	   */
	  public Hashtable<String ,Object> getThreadContext() {
	    return threadContext;
	  }
	}