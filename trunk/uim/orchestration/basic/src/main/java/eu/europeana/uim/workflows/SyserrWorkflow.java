package eu.europeana.uim.workflows;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 *  Workflow to write to system error.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 4, 2011
 */
public class SyserrWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    public SyserrWorkflow(int batchSize, boolean randsleep) {
        super(SyserrWorkflow.class.getSimpleName(),
                "Simple workflow which uses a SyserrPlugins to fail all records");
        setStart(new BatchWorkflowStart());
        addStep(new SyserrPlugin());
    }

    @Override
    public boolean isSavepoint(IngestionPlugin plugin) {
        return true;
    }

    @Override
    public boolean isMandatory(IngestionPlugin plugin) {
        return false;
    }
}
