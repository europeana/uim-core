package eu.europeana.uim.workflow;

import java.io.Serializable;
import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;

/**
 * The runtime status of a workflow execution
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStepStatus extends Serializable {

    IngestionPlugin getStep();

    int successes();
    int failures();

    Map<MetaDataRecord, Throwable> getFailureDetail();
}
