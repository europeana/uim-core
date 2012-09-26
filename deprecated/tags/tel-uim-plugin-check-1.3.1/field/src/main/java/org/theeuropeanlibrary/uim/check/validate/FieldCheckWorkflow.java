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
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class FieldCheckWorkflow extends AbstractWorkflow {
    /**
     * Creates a new instance of this class.
     */
    public FieldCheckWorkflow() {
        super("C: Field Validation",
                "Workflow which is used to validate fields of stord meta data records.");

        setStart(new BatchWorkflowStart());
        addStep(new FieldCheckIngestionPlugin());
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
