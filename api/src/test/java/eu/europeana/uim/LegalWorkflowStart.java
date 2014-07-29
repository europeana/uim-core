/* LegalWorkflowStart.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.Collections;
import java.util.List;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.source.AbstractWorkflowStart;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.plugin.source.WorkflowStartFailedException;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * This is a minimal workflow start. This should not throw an exception.
 *
 * @param <I> generic identifier
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class LegalWorkflowStart<I> extends AbstractWorkflowStart<MetaDataRecord<I>, I> {

    /**
     * Creates a new instance of this class.
     */
    public LegalWorkflowStart() {
        super("Legal workflow start", "Legal workflow start description");
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
    public TaskCreator<MetaDataRecord<I>, I> createLoader(ExecutionContext<MetaDataRecord<I>, I> context)
            throws WorkflowStartFailedException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public boolean isFinished(ExecutionContext<MetaDataRecord<I>, I> context) {
        return false;
    }

    @Override
    public void initialize(ExecutionContext<MetaDataRecord<I>, I> context) throws WorkflowStartFailedException {
    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context) throws WorkflowStartFailedException {
    }

    @Override
    public int getTotalSize(ExecutionContext<MetaDataRecord<I>, I> context) {
        return 0;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }
}
