package eu.europeana.uim.logging.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

/**
 * Database backend of {@link LoggingEngine} using JPA for object relational mapping.
 * 
 * @param <T>
 *            generic message
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
public class DatabaseLoggingEngine<T extends Serializable> implements LoggingEngine<Long, T> {
    private TStringDatabaseLogEntryHome stringHome;
    private TObjectDatabaseLogEntryHome objectHome;
    private TDurationDatabaseEntryHome  durationHome;

    @Override
    public String getIdentifier() {
        return DatabaseLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(Level level, String message, Execution<Long> execution,
            MetaDataRecord<Long> mdr, IngestionPlugin plugin) {
        TStringDatabaseLogEntry entry = new TStringDatabaseLogEntry();
        entry.setLevel(level);
        entry.setDate(new Date());
        entry.setExecutionId(execution.getId());
        entry.setPluginName(plugin.getName());
        entry.setMetaDataRecordId(mdr.getId());
        entry.setMessage(message);

        stringHome.update(entry);
    }

    @Override
    public void logStructured(Level level, T payload, Execution<Long> execution,
            MetaDataRecord<Long> mdr, IngestionPlugin plugin) {
        TObjectDatabaseLogEntry entry = new TObjectDatabaseLogEntry();
        entry.setLevel(level);
        entry.setDate(new Date());
        entry.setExecutionId(execution.getId());
        entry.setPluginName(plugin.getName());
        entry.setMetaDataRecordId(mdr.getId());
        entry.setMessage(payload);

        objectHome.update(entry);
    }

    @Override
    public List<LogEntry<Long, String>> getExecutionLog(Execution<Long> execution) {
        List<TStringDatabaseLogEntry> entries = stringHome.findByExecution(execution.getId());
        List<LogEntry<Long, String>> results = new ArrayList<LogEntry<Long, String>>();
        for (TDatabaseLogEntry<?> entry : entries) {
            if (entry.getMessage() instanceof String) {
                results.add((TStringDatabaseLogEntry)entry);
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LogEntry<Long, T>> getStructuredExecutionLog(Execution<Long> execution) {
        List<TObjectDatabaseLogEntry> entries = objectHome.findByExecution(execution.getId());
        List<LogEntry<Long, T>> results = new ArrayList<LogEntry<Long, T>>();
        for (TDatabaseLogEntry<?> entry : entries) {
            if (!(entry.getMessage() instanceof String)) {
                results.add((LogEntry<Long, T>)entry);
            }
        }
        return results;
    }

    @Override
    public void logDuration(IngestionPlugin plugin, Long duration, int count) {
        TDurationDatabaseEntry[] entries = new TDurationDatabaseEntry[count];
        for (int i = 0; i < count; i++) {
            TDurationDatabaseEntry entry = new TDurationDatabaseEntry();
            entry.setDuration(duration / count);
            entry.setPluginName(plugin.getName());
            entries[i] = entry;
        }
        durationHome.insert(entries);
    }

    @Override
    public void logDurationDetailed(IngestionPlugin plugin, Long duration, Long... mdr) {
        TDurationDatabaseEntry[] entries = new TDurationDatabaseEntry[mdr.length];
        for (int i = 0; i < mdr.length; i++) {
            TDurationDatabaseEntry entry = new TDurationDatabaseEntry();
            entry.setDuration(duration / mdr.length);
            entry.setPluginName(plugin.getName());
            entries[i] = entry;
        }
        durationHome.insert(entries);
    }

    @Override
    public Long getAverageDuration(IngestionPlugin plugin) {
        long average = 0;
        List<TDurationDatabaseEntry> entries = durationHome.findByPlugin(plugin.getName());
        for (TDurationDatabaseEntry entry : entries) {
            average += entry.getDuration();
        }
        average /= entries.size();
        return average;
    }
}
