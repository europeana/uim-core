/* LoggingEngineAdapter.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.util.List;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Dummy implementation of logging engine.
 * 
 * @param <I>
 *            generic identifier
 * @param <T>
 *            generic message
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public abstract class LoggingEngineAdapter<I, T> implements LoggingEngine<I, T> {
    @Override
    public String getIdentifier() {
        return LoggingEngineAdapter.class.getSimpleName();
    }

    @Override
    public void log(String module, Execution<I> execution,
            String scope, Level level, String... message) {
    }

    @Override
    public void log(IngestionPlugin plugin, Execution<I> execution, MetaDataRecord<I> mdr,
            String scope, Level level, String... message) {
    }

    @Override
    public List<LogEntry<I, String[]>> getExecutionLog(Execution<I> execution) {
        return null;
    }

    @Override
    public List<LogEntry<I, String[]>> getExecutionLog(I executionID) {
        return null;
    }
    
    @Override
    public void logStructured(IngestionPlugin plugin, Execution<I> execution,
            MetaDataRecord<I> mdr, String scope, Level level, T payload) {
    }

    @Override
    public List<LogEntry<I, T>> getStructuredExecutionLog(Execution<I> execution) {
        return null;
    }

    @Override
    public List<LogEntry<I, T>> getStructuredExecutionLog(I executionID) {
        return null;
    }
    
    @Override
    public void logDuration(IngestionPlugin plugin, Long duration, int count) {
        //
    }

    @Override
    public Long getAverageDuration(IngestionPlugin plugin) {
        return null;
    }

    @Override
    public void logDurationDetailed(IngestionPlugin plugin, Long duration, I... mdr) {
        //
    }
}
