package eu.europeana.uim.plugin.source;

/**
 * Task status as enumeration for predefined states a task can be in.
 * 
 * @author Manuel Bernhardt<bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public enum TaskStatus {
    /**
     * the task has just been created and is therefore new
     */
    NEW,
    /**
     * the task is processed at the moment
     */
    PROCESSING,
    /**
     * the task is queued and ready to work
     */
    QUEUED,
    /**
     * the task has failed and should be neglected
     */
    FAILED,
    /**
     * the task has finished, it is done
     */
    DONE
}
