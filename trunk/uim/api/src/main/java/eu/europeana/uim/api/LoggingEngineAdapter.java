/* LoggingEngineAdapter.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.util.List;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Execution;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @param <T> 
 * @date Feb 16, 2011
 */
public abstract class LoggingEngineAdapter<T> implements LoggingEngine<T> {

    @Override
	public String getIdentifier() {
		return LoggingEngineAdapter.class.getSimpleName();
	}

	@Override
	public void log(Level level, String message, Execution execution,
			MetaDataRecord mdr, IngestionPlugin plugin) {
	}

	@Override
	public List<LogEntry<String>> getExecutionLog(Execution execution) {
		 return null;
	}

	@Override
	public void logStructured(Level level, Object payload, Execution execution,
			MetaDataRecord mdr, IngestionPlugin plugin) {
	}

	@Override
	public List<LogEntry<T>> getStructuredExecutionLog(Execution execution) {
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
	public void logDuration(IngestionPlugin plugin, Long duration, long... mdr) {
		// 
	}

}
