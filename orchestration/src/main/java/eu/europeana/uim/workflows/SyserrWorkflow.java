package eu.europeana.uim.workflows;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.util.BatchWorkflowStart;
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
public class SyserrWorkflow<I> extends AbstractWorkflow<MetaDataRecord<I>, I> {
    /**
     * Creates a new instance of this class.
     */
    public SyserrWorkflow() {
        this(250, true);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param batchSize
     * @param randsleep
     */
    public SyserrWorkflow(int batchSize, boolean randsleep) {
        super("XT: Syserr Workflow",
                "Simple workflow which uses a SyserrPlugins to fail all records");
        setStart(new BatchWorkflowStart<I>());
        addStep(new SyserrPlugin<MetaDataRecord<I>, I>());
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
