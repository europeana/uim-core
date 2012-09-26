package eu.europeana.uim.workflow;

import java.io.Serializable;
import java.util.Map;

import eu.europeana.uim.plugin.ExecutionPlugin;
import eu.europeana.uim.store.UimDataSet;

/**
 * The runtime status of a workflow execution
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public interface WorkflowStepStatus<U extends UimDataSet<I>, I> extends Serializable {
    /**
     * @return retrieves a plugin
     */
    ExecutionPlugin<U, I> getStep();

    /**
     * @return How many successes?
     */
    int successes();

    /**
     * @return How many failures?
     */
    int failures();

    /**
     * @return throwable for failed records, empty if no failures occurred
     */
    Map<U, Throwable> getFailureDetail();
}
