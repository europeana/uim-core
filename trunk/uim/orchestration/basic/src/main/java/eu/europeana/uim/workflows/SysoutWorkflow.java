package eu.europeana.uim.workflows;

import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.util.LoggingIngestionPlugin;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * Workflow to write to system out.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class SysoutWorkflow extends AbstractWorkflow {
    private String savePointIdentifier;

    /**
     * Creates a new instance of this class.
     */
    public SysoutWorkflow() {
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
        super("XT: Console Reporting Workflow",
                "Simple workflow which uses sysout and logging plugins to report to the console about processing!");
        setStart(new BatchWorkflowStart());

        SysoutPlugin step = new SysoutPlugin();
        addStep(step);

        LoggingIngestionPlugin log = new LoggingIngestionPlugin();
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
