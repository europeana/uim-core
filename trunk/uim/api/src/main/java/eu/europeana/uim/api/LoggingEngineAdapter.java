/* LoggingEngineAdapter.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.util.List;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Execution;

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
    public void log(Level level, String message, Execution<I> execution, MetaDataRecord<I> mdr,
            IngestionPlugin plugin) {
    }

    @Override
    public List<LogEntry<I, String>> getExecutionLog(Execution<I> execution) {
        return null;
    }

    @Override
    public void logStructured(LoggingEngine.Level level, T payload, Execution<I> execution,
            MetaDataRecord<I> mdr, IngestionPlugin plugin) {
    }

    @Override
    public List<LogEntry<I, T>> getStructuredExecutionLog(Execution<I> execution) {
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
