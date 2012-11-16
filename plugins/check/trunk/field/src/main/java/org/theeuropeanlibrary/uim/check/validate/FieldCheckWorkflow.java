/* LinkCheckingWorkflow.java - created on Apr 6, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.validate;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.util.BatchWorkflowStart;
import eu.europeana.uim.util.LoggingIngestionPlugin;
import eu.europeana.uim.workflow.AbstractWorkflow;

/**
 * This workflow processes the records, validates fields on a {@link MetaDataRecord} and logs the
 * result.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class FieldCheckWorkflow<I> extends AbstractWorkflow<MetaDataRecord<I>, I> {
    /**
     * Creates a new instance of this class.
     */
    public FieldCheckWorkflow() {
        super("N: Field Validation",
                "Workflow which is used to validate fields of stord meta data records.");

        setStart(new BatchWorkflowStart<I>());
        addStep(new FieldCheckIngestionPlugin<I>());
        addStep(new LoggingIngestionPlugin<MetaDataRecord<I>, I>());
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
