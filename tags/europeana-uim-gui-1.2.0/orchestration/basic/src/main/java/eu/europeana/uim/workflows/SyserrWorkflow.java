package eu.europeana.uim.workflows;

import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 *  Workflow to write to system error.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class SyserrWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    @SuppressWarnings("rawtypes")
    public SyserrWorkflow(int batchSize, boolean randsleep) {
        super("Error Console Reporting Workflow",
                "Simple workflow which uses a SyserrPlugins to fail all records");
        setStart(new BatchWorkflowStart());
        addStep(new SyserrPlugin());
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        return true;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
