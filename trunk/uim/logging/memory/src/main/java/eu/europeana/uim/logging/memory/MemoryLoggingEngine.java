package eu.europeana.uim.logging.memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

/**
 * Simplistic implementation of the logging service.
 * In this implementation we do not care to keep track of the MDRs responsible for a duration. This feature would be useful in order to see exactly what MDR is causing what delay.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MemoryLoggingEngine<T> implements LoggingEngine<T> {

    private Map<Execution, List<LogEntry<String>>> executionLogs = new HashMap<Execution, List<LogEntry<String>>>();

    private Map<Execution, List<LogEntry<T>>> structuredExecutionLogs = new HashMap<Execution, List<LogEntry<T>>>();

    private Map<IngestionPlugin, List<Long>> durations = new HashMap<IngestionPlugin, List<Long>>();

    
    
    public MemoryLoggingEngine() {
	}

    @Override
    public String getIdentifier() {
        return MemoryLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(Level level, String message, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin) {
        List<LogEntry<String>> logs = executionLogs.get(execution);
        if (logs == null) {
            logs = new ArrayList<LogEntry<String>>();
            executionLogs.put(execution, logs);
        }
        logs.add(new MemoryLogEntry<String>(level, new Date(), execution, plugin, mdr, message));
    }

    @Override
    public void logStructured(Level level, T payload, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin) {
        List<LogEntry<T>> logs = structuredExecutionLogs.get(execution);
        if (logs == null) {
            logs = new ArrayList<LogEntry<T>>();
            structuredExecutionLogs.put(execution, logs);
        }
        logs.add(new MemoryLogEntry<T>(level, new Date(), execution, plugin, mdr, payload));
    }

    @Override
    public List<LogEntry<String>> getExecutionLog(Execution execution) {
        return executionLogs.get(execution);
    }

    @Override
    public List<LogEntry<T>> getStructuredExecutionLog(Execution execution) {
        return structuredExecutionLogs.get(execution);
    }

    private List<Long> getDurations(IngestionPlugin plugin) {
        List<Long> d = durations.get(plugin);
        if (d == null) {
            d = new ArrayList<Long>();
            durations.put(plugin, d);
        }
        return d;
    }

    @Override
    public void logDuration(IngestionPlugin plugin, Long duration, long... mdrs) {
        List<Long> d = getDurations(plugin);
        // don't show this to hardcore statisticians
        for (int i = 0; i < mdrs.length; i++) {
            d.add(duration / mdrs.length);
        }
    }

    @Override
    public void logDuration(IngestionPlugin plugin, Long duration, int count) {
        List<Long> d = getDurations(plugin);
        // don't show this to hardcore statisticians
        for (int i = 0; i < count; i++) {
            d.add(duration / count);
        }
    }

    @Override
    public Long getAverageDuration(IngestionPlugin plugin) {
        long sum = 0l;
        List<Long> d = getDurations(plugin);
        for(Long l : d) {
            sum += l;
        }
        return sum / d.size();
    }
}
