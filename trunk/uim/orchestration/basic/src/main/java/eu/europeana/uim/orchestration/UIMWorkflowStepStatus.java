package eu.europeana.uim.orchestration;

import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.workflow.WorkflowStep;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class UIMWorkflowStepStatus implements WorkflowStepStatus {

	private WorkflowStep step, parent;
	private int successes, failures;
	private Map<MetaDataRecord, Throwable> failureDetail;


	public UIMWorkflowStepStatus(WorkflowStep step, int successes, int failures, Map<MetaDataRecord, Throwable> failureDetail) {
        this.step = step;
        this.successes = successes;
        this.failures = failures;
        this.failureDetail = failureDetail;
    }

    @Override
    public WorkflowStep getStep() {
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
    public Map<MetaDataRecord, Throwable> getFailureDetail() {
        return failureDetail;
    }

    @Override
    public WorkflowStep getParent() {
        return parent;
    }

    public void setParent(WorkflowStep parent) {
        this.parent = parent;
    }
}
