package eu.europeana.uim.workflow.dummy;

import java.util.logging.Logger;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;
import eu.europeana.uim.workflow.IngestionWorkflowStep;
import eu.europeana.uim.workflow.Workflow;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class DummyWorkflow extends AbstractWorkflow implements Workflow {

    private static Logger log = Logger.getLogger(DummyWorkflow.class.getSimpleName());

    public DummyWorkflow(Registry registry) {
    	super(DummyWorkflow.class.getName(),"This awesome workflow demonstrates the capabilities of the UIM");
    	
    	setStart(new BatchWorkflowStart());
    	
        // that's a very exciting worklow
        IngestionPlugin plugin1 = registry.getPlugin("DummyPlugin");
        IngestionPlugin plugin2 = registry.getPlugin("DummyPlugin");
        
        addStep(new IngestionWorkflowStep(plugin1));
        addStep(new IngestionWorkflowStep(plugin2));
    }



}
