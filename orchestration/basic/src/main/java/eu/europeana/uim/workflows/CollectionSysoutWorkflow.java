package eu.europeana.uim.workflows;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.util.CollectionBatchWorkflowStart;
import eu.europeana.uim.util.LoggingIngestionPlugin;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system out.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class CollectionSysoutWorkflow<I> extends AbstractWorkflow<Collection<I>, I> {
    private String savePointIdentifier;

    /**
     * Creates a new instance of this class.
     */
    public CollectionSysoutWorkflow() {
        super("XT: Sysout Workflow",
                "Simple workflow which uses sysout and logging plugins to report to the console about processing!");
        setStart(new CollectionBatchWorkflowStart<I>());

        SysoutPlugin<Collection<I>, I> step = new SysoutPlugin<Collection<I>, I>();
        addStep(step);

        LoggingIngestionPlugin<Collection<I>, I> log = new LoggingIngestionPlugin<Collection<I>, I>();
        addStep(log);

        savePointIdentifier = step.getIdentifier();
    }

    @Override
    public boolean isSavepoint(String pluginIdentifier) {
        if (pluginIdentifier.equals(savePointIdentifier)) { return true; }
        return false;
    }

    @Override
    public boolean isMandatory(String pluginIdentifier) {
        return false;
    }
}
