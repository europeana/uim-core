package eu.europeana.uim.workflows;

import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;
import eu.europeana.uim.workflow.IngestionWorkflowStep;

public class SyserrWorkflow extends AbstractWorkflow {

	public SyserrWorkflow(int batchSize, boolean randsleep) {
		super(SyserrWorkflow.class.getSimpleName(), "Simple workflow which uses a SyserrPlugins to fail all records");
		setStart(new BatchWorkflowStart(batchSize));

		addStep(new IngestionWorkflowStep(new SyserrPlugin("", 1, randsleep)));

	}


}
