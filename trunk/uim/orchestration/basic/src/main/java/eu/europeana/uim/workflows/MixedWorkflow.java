package eu.europeana.uim.workflows;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

public class MixedWorkflow extends AbstractWorkflow {

    public MixedWorkflow(int batchSize, boolean randsleep) {
        super(MixedWorkflow.class.getSimpleName(), "Simple workflow which uses several SysoutPlugins to report to the console about processing");
        setStart(new BatchWorkflowStart());

        List<IngestionPlugin> plugins = new ArrayList<IngestionPlugin>();
        plugins.add(new SysoutPlugin());
        plugins.add(new SyserrPlugin());

        for (IngestionPlugin step : plugins) {
            addStep(step);
        }
    }

    @Override
    public boolean isSavepoint(IngestionPlugin plugin) {
        if (plugin instanceof SyserrPlugin) {
            return true;
        }
        return false;
    }


}
