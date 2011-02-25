package eu.europeana.uim.workflows;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

public class SyserrWorkflow extends AbstractWorkflow {

	public SyserrWorkflow(int batchSize, boolean randsleep) {
		super(SyserrWorkflow.class.getSimpleName(), "Simple workflow which uses a SyserrPlugins to fail all records");
		setStart(new BatchWorkflowStart());

		addStep(new SyserrPlugin());
	}


    @Override
    public boolean isSavepoint(IngestionPlugin plugin) {
        return true;
    }


}
