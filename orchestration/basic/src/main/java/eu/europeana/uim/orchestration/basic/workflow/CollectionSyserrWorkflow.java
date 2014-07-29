package eu.europeana.uim.orchestration.basic.workflow;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.util.CollectionBatchWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system error.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class CollectionSyserrWorkflow<I> extends AbstractWorkflow<Collection<I>, I> {
    /**
     * Creates a new instance of this class.
     */
    public CollectionSyserrWorkflow() {
        this(250, true);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    public CollectionSyserrWorkflow(int batchSize, boolean randsleep) {
        super("XT: Syserr Workflow",
                "Simple workflow which uses a SyserrPlugins to fail all records");
        setStart(new CollectionBatchWorkflowStart<I>());
        addStep(new SyserrPlugin<Collection<I>, I>());
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        return true;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
