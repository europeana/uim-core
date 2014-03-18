package eu.europeana.uim.orchestration.basic;

import java.util.Map;

import eu.europeana.uim.plugin.ExecutionPlugin;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.WorkflowStepStatus;

/**
 * Status of a specific workflow step on the UIM pipeline.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic ID
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class UIMWorkflowStepStatus<U extends UimDataSet<I>, I> implements WorkflowStepStatus<U, I> {
    private ExecutionPlugin<U, I> step;
    private int                   successes, failures;
    private Map<U, Throwable>     failureDetail;

    /**
     * Creates a new instance of this class.
     * 
     * @param step
     * @param successes
     * @param failures
     * @param failureDetail
     */
    public UIMWorkflowStepStatus(ExecutionPlugin<U, I> step, int successes, int failures,
                                 Map<U, Throwable> failureDetail) {
        this.step = step;
        this.successes = successes;
        this.failures = failures;
        this.failureDetail = failureDetail;
    }

    @Override
    public ExecutionPlugin<U, I> getStep() {
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
    public Map<U, Throwable> getFailureDetail() {
        return failureDetail;
    }
}
