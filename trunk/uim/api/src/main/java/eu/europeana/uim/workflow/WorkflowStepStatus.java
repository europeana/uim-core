package eu.europeana.uim.workflow;

import java.io.Serializable;
import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;

/**
 * The runtime status of a workflow execution
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 4, 2011
 */
public interface WorkflowStepStatus extends Serializable {
    /**
     * @return retrieves a plugin
     */
    IngestionPlugin getStep();

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
    Map<MetaDataRecord, Throwable> getFailureDetail();
}
