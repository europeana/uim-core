package eu.europeana.uim.orchestration;

import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * Status of a specific workflow step on the UIM pipeline.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
public class UIMWorkflowStepStatus implements WorkflowStepStatus {
    private IngestionPlugin                   step;
    private int                               successes, failures;
    private Map<MetaDataRecord<?>, Throwable> failureDetail;

    /**
     * Creates a new instance of this class.
     * 
     * @param step
     * @param successes
     * @param failures
     * @param failureDetail
     */
    public UIMWorkflowStepStatus(IngestionPlugin step, int successes, int failures,
                                 Map<MetaDataRecord<?>, Throwable> failureDetail) {
        this.step = step;
        this.successes = successes;
        this.failures = failures;
        this.failureDetail = failureDetail;
    }

    @Override
    public IngestionPlugin getStep() {
        return step;
    }

    @Override
    public int successes() {
        return successes;
    }

    @Override
    public int failures() {
        return failures;
    }

    @Override
    public Map<MetaDataRecord<?>, Throwable> getFailureDetail() {
        return failureDetail;
    }
}
