package eu.europeana.uim.workflows;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

public class SysoutWorkflow extends AbstractWorkflow {

    public SysoutWorkflow(Registry registry) {
        this(50, false, false);
    }

    public SysoutWorkflow(int batchSize, boolean randsleep, boolean savepoint) {
        super(SysoutWorkflow.class.getSimpleName(), "Simple workflow which uses several SysoutPlugins to report to the console about processing");
        setStart(new BatchWorkflowStart());

        addStep(new SysoutPlugin());

    }


    @Override
    public boolean isSavepoint(IngestionPlugin plugin) {
        if (plugin instanceof SysoutPlugin) {
            return true;
        }
        return false;
    }


}
