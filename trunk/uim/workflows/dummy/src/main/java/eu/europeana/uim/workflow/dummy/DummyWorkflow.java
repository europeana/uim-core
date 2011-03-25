package eu.europeana.uim.workflow.dummy;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;
import eu.europeana.uim.workflow.Workflow;

/**
 * Dummy workflow to demonstrate UIM. It does more or less nothing useful.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class DummyWorkflow extends AbstractWorkflow implements Workflow {
    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public DummyWorkflow(Registry registry) {
        super(DummyWorkflow.class.getName(),
                "This awesome workflow demonstrates the capabilities of the UIM");

        setStart(new BatchWorkflowStart());

        // that's a very exciting worklow
        IngestionPlugin plugin = registry.getPlugin("DummyPlugin");
        addStep(plugin);
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        return false;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
