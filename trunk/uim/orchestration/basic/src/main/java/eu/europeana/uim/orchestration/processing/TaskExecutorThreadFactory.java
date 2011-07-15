package eu.europeana.uim.orchestration.processing;

import eu.europeana.uim.common.SimpleThreadFactory;

/**
 * Factory pattern to provide newly created threads for task execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class TaskExecutorThreadFactory extends SimpleThreadFactory {

    /**
     * Creates a new instance of this class.
     */
    public TaskExecutorThreadFactory() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param threadname
     */
    public TaskExecutorThreadFactory(String threadname) {
        super(threadname);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupname
     * @param threadname
     */
    public TaskExecutorThreadFactory(String groupname, String threadname) {
        super(groupname, threadname);
    }

    
}
