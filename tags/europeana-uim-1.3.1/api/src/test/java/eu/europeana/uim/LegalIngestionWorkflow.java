/* LegalWorkflow.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * This is a minimal workflow. This should not throw an exception.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class LegalIngestionWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     */
    public LegalIngestionWorkflow() {
        super("Legal Ingestion Workflow", "Legal Ingestion Workflow description");
        setStart(new LegalWorkflowStart());
        addStep(new LegalIngestionPlugin());
    }

    @Override
    public String getIdentifier() {
        return LegalIngestionWorkflow.class.getSimpleName();
    }

    @Override
    public boolean isSavepoint(String pluginIdentifier) {
        return false;
    }

    @Override
    public boolean isMandatory(String pluginIdentifier) {
        return false;
    }
}
