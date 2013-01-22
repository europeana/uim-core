/* LinkCheckingWorkflow.java - created on Apr 6, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.util.LoggingIngestionPlugin;
import eu.europeana.uim.util.RecordAwareCBWorkflowStart;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * This workflow processes the records, checks which links are working and logs the result
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class LinkCheckWorkflow<I> extends AbstractWorkflow<Collection<I>, I> {
    /**
     * Creates a new instance of this class.
     */
    public LinkCheckWorkflow() {
        super("C: Link Validation", "Workflow which is used to submit links to be checked.");

        setStart(new RecordAwareCBWorkflowStart<I>());
        addStep(new LinkCheckIngestionPlugin<I>());
        //addStep(new LoggingIngestionPlugin<MetaDataRecord<I>, I>());
    }

    /* (non-Javadoc)
     * @see eu.europeana.uim.workflow.Workflow#isSavepoint(java.lang.String)
     */
    @Override
    public boolean isSavepoint(String pluginName) {
        return false;
    }

    /* (non-Javadoc)
     * @see eu.europeana.uim.workflow.Workflow#isMandatory(java.lang.String)
     */
    @Override
    public boolean isMandatory(String pluginName) {
        return false;
    }
}
