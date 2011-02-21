package eu.europeana.uim.workflows;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;
import eu.europeana.uim.workflow.IngestionWorkflowStep;
import eu.europeana.uim.workflow.WorkflowStep;
import eu.europeana.uim.workflow.WorkflowStepComposite;

public class MixedWorkflow extends AbstractWorkflow {

	public MixedWorkflow(int batchSize, boolean composite, boolean randsleep) {
		super(MixedWorkflow.class.getSimpleName(), "Simple workflow which uses several SysoutPlugins to report to the console about processing");
		setStart(new BatchWorkflowStart(batchSize));

		List<WorkflowStep> plugins = new ArrayList<WorkflowStep>();
		plugins.add(new IngestionWorkflowStep(new SysoutPlugin("1", randsleep)));
		plugins.add(new IngestionWorkflowStep(new SysoutPlugin("2", randsleep)));
		plugins.add(new IngestionWorkflowStep(new SyserrPlugin("", 2, randsleep)));

		
		if (composite) {
			WorkflowStepComposite onestep = new WorkflowStepComposite();
			for (WorkflowStep step : plugins) {
				onestep.addStep(step);
			}
			addStep(onestep);
		} else {
			for (WorkflowStep step : plugins) {
				addStep(step);
			}
		}
	}


}
