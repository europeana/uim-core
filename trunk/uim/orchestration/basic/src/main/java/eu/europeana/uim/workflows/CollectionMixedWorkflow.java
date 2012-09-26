package eu.europeana.uim.workflows;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.util.CollectionBatchWorkflowStart;
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
public class CollectionMixedWorkflow<I> extends AbstractWorkflow<Collection<I>, I> {
    /**
     * Creates a new instance of this class.
     */
    public CollectionMixedWorkflow() {
        this(250, true);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    public CollectionMixedWorkflow(int batchSize, boolean randsleep) {
        super("XT: Sysout/Syserr Workflow",
                "Simple workflow which uses several SysoutPlugins to report to the console about processing");
        setStart(new CollectionBatchWorkflowStart<I>());

        addStep(new SysoutPlugin<Collection<I>, I>());
        addStep(new SyserrPlugin<Collection<I>, I>());
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
