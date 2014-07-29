package eu.europeana.uim.orchestration.basic.workflow;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system out and system error.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class MixedWorkflow<I> extends AbstractWorkflow<MetaDataRecord<I>, I> {
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
        setStart(new BatchWorkflowStart<I>());

        addStep(new SysoutPlugin<MetaDataRecord<I>, I>());
        addStep(new SyserrPlugin<MetaDataRecord<I>, I>());
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        if (pluginName.equals(new SyserrPlugin<MetaDataRecord<I>, I>().getIdentifier())) { return true; }
        return false;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
