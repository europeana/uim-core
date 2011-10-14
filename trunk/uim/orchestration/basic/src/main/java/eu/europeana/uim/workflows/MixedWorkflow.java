package eu.europeana.uim.workflows;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system out and system error.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class MixedWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     */
    public MixedWorkflow() {
        this(250, true);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    public MixedWorkflow(int batchSize, boolean randsleep) {
        super("XT: Sysout/Syserr Workflow",
                "Simple workflow which uses several SysoutPlugins to report to the console about processing");
        setStart(new BatchWorkflowStart());

        List<IngestionPlugin> plugins = new ArrayList<IngestionPlugin>();
        plugins.add(new SysoutPlugin());
        plugins.add(new SyserrPlugin());

        for (IngestionPlugin step : plugins) {
            addStep(step);
        }
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        if (pluginName.equals(new SyserrPlugin().getIdentifier())) { return true; }
        return false;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
