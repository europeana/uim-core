package eu.europeana.uim.workflows;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system out.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class SysoutWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public SysoutWorkflow(Registry registry) {
        this(50, false, false);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     * @param savepoint
     */
    public SysoutWorkflow(int batchSize, boolean randsleep, boolean savepoint) {
        super(SysoutWorkflow.class.getSimpleName(),
                "Simple workflow which uses several SysoutPlugins to report to the console about processing");
        setStart(new BatchWorkflowStart());

        addStep(new SysoutPlugin());

    }

    @Override
    public boolean isSavepoint(String pluginName) {
        if (pluginName.equals(new SysoutPlugin().getName())) { return true; }
        return false;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
