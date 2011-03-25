package eu.europeana.uim.orchestration.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Factory pattern to provide newly created threads for task execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class TaskExecutorThreadFactory implements ThreadFactory {
    private static long              id               = 0;

    private String                   groupname        = "anonymouse";
    private String                   threadname       = "anonymouse";

    private ThreadGroup              threadgroup;

    private List<TaskExecutorThread> deliveredThreads = new LinkedList<TaskExecutorThread>();

    /**
     * Creates a new instance of this class.
     */
    public TaskExecutorThreadFactory() {
        threadgroup = new ThreadGroup(groupname);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param threadname
     */
    public TaskExecutorThreadFactory(String threadname) {
        this.threadname = threadname;
        threadgroup = new ThreadGroup(groupname);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupname
     * @param threadname
     */
    public TaskExecutorThreadFactory(String groupname, String threadname) {
        this.groupname = groupname;
        this.threadname = threadname;
        threadgroup = new ThreadGroup(groupname);
    }

    @Override
    public Thread newThread(Runnable runable) {
        TaskExecutorThread worker = new TaskExecutorThread(threadgroup, runable, threadname + "[" +
                                                                                 id++ + "]");
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
