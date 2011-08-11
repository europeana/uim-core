/* LegalWorkflowStart.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.Collections;
import java.util.List;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.WorkflowStart;
import eu.europeana.uim.workflow.WorkflowStartFailedException;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class LegalWorkflowStart implements WorkflowStart {

    @Override
    public String getIdentifier() {
        return LegalWorkflowStart.class.getSimpleName();
    }

    @Override
    public String getName() {
        return "legal workflow start";
    }

    @Override
    public String getDescription() {
        return "legal workflow start description";
    }

    @Override
    public List<String> getParameters() {
         return Collections.emptyList();
    }

    @Override
    public int getPreferredThreadCount() {
         return 1;
    }

    @Override
    public int getMaximumThreadCount() {
        return 5;
    }

    @Override
    public <I> TaskCreator<I> createLoader(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <I> boolean isFinished(ExecutionContext<I> context, StorageEngine<I> storage) {
         return false;
    }

    @Override
    public <I> void initialize(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException {
    }

    @Override
    public <I> void completed(ExecutionContext<I> context, StorageEngine<I> storage) throws WorkflowStartFailedException {
    }

    @Override
    public <I> int getTotalSize(ExecutionContext<I> context) {
         return 0;
    }

}
