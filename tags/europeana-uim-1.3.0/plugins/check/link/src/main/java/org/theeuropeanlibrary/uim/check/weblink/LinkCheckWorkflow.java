/* LinkCheckingWorkflow.java - created on Apr 6, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.util.LoggingIngestionPlugin;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * This workflow processes the records, checks which links are working and logs the result
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class LinkCheckWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     */
    public LinkCheckWorkflow() {
        super("C: Link Validation",
                "Workflow which is used to submit links to be checked.");

        setStart(new BatchWorkflowStart());
        addStep(new LinkCheckIngestionPlugin());
        addStep(new LoggingIngestionPlugin());
    }

    @Override
    public boolean isSavepoint(String pluginName) {
        return false;
    }

    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
