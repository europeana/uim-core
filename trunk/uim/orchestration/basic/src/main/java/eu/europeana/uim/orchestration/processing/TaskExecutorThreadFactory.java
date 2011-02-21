package eu.europeana.uim.orchestration.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class TaskExecutorThreadFactory implements ThreadFactory {
		  
		  private static long id = 0;
		  
		  private String groupname = "anonymouse";
		  private String threadname = "anonymouse";
		  
		  private ThreadGroup threadgroup;
		  
		  private List<TaskExecutorThread> deliveredThreads = new LinkedList<TaskExecutorThread>();

		  public TaskExecutorThreadFactory(){
		    threadgroup = new ThreadGroup(groupname);
		  }
		  
		  public TaskExecutorThreadFactory(String threadname){
		    this.threadname = threadname;
		    threadgroup = new ThreadGroup(groupname);
		  }
		  
		  public TaskExecutorThreadFactory(String groupname, String threadname){
		    this.groupname = groupname;
		    this.threadname = threadname;
		    threadgroup = new ThreadGroup(groupname);
		  }
		  
		  
		  /* (non-Javadoc)
		   * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		   */
		  public Thread newThread(Runnable runable) {
		    TaskExecutorThread worker = new TaskExecutorThread(threadgroup, runable, threadname + "[" + id++ + "]");
		    deliveredThreads.add(worker);
		    return worker;
		  }

		  /**
		   * @return the groupname
		   */
		  public String getGroupname() {
		    return groupname;
		  }

		  /**
		   * @return the threadname
		   */
		  public String getThreadname() {
		    return threadname;
		  }
}
