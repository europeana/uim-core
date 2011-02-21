package eu.europeana.uim.workflow;

import java.io.Serializable;
import java.util.Map;

import eu.europeana.uim.MetaDataRecord;

/**
 * The runtime status of a workflow execution
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStepStatus extends Serializable {

    WorkflowStep getStep();

    /** parent step, for now this can only be a ProcessingContainer **/
    WorkflowStep getParent();

    int successes();
    int failures();

    Map<MetaDataRecord, Throwable> getFailureDetail();
}
