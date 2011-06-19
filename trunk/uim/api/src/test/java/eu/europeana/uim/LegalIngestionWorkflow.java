/* LegalWorkflow.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class LegalIngestionWorkflow implements Workflow {

    @Override
    public String getIdentifier() {
         return LegalIngestionWorkflow.class.getSimpleName();
    }

    @Override
    public String getName() {
         return "Legal Ingestion Workflow";
    }
    

    @Override
    public String getDescription() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public WorkflowStart getStart() {
         return new LegalWorkflowStart();
    }

    @Override
    public List<IngestionPlugin> getSteps() {
        List<IngestionPlugin> steps = new ArrayList<IngestionPlugin>();
        steps.add(new LegalIngestionPlugin());
        return steps;
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
