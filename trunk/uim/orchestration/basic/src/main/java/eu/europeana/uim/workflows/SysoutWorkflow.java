package eu.europeana.uim.workflows;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;
import eu.europeana.uim.workflow.IngestionWorkflowStep;

public class SysoutWorkflow extends AbstractWorkflow {
	
	public SysoutWorkflow(Registry registry) {
		this(1, 50, false, false);
	}

	public SysoutWorkflow(int plugins, int batchSize, boolean randsleep, boolean savepoint) {
		super(SysoutWorkflow.class.getSimpleName(), "Simple workflow which uses several SysoutPlugins to report to the console about processing");
		setStart(new BatchWorkflowStart(batchSize));
		
		for (int i = 0 ; i < plugins; i++){
			addStep(new IngestionWorkflowStep(new SysoutPlugin("" + i, randsleep), savepoint));
		}
		
	}


}
