package eu.europeana.uim.logging.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.common.BlockingInitializer;
import eu.europeana.uim.logging.database.model.TDatabaseLogEntry;
import eu.europeana.uim.logging.database.model.TDurationDatabaseEntry;
import eu.europeana.uim.logging.database.model.TObjectDatabaseLogEntry;
import eu.europeana.uim.logging.database.model.TStringDatabaseLogEntry;
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
    private static final Logger  log                 = Logger.getLogger(DatabaseLoggingEngine.class.getName());

    private DatabaseLoggingStorage storage;

    /**
     * Creates a new instance of this class. The default constructor is used to initialize the
     * spring context and alike for the storage engine.
     */
    public DatabaseLoggingEngine() {
        BlockingInitializer initializer = new BlockingInitializer() {
            @Override
            public void initialize() {
                try {
                    status = STATUS_BOOTING;
                    
                    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
                    context.setValidating(false);
                    context.setConfigLocations(
                            new String[]{"/META-INF/db-context.xml", "/META-INF/db-beans.xml"});
                    context.refresh();

                    storage = (DatabaseLoggingStorage)context.getAutowireCapableBeanFactory().autowire(
                            DatabaseLoggingStorage.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
                    
                    status = STATUS_INITIALIZED;
                } catch (Throwable t) {
                    log.log(java.util.logging.Level.SEVERE, "Failed to initialize database logging.", t);
                    status = STATUS_FAILED;
                }
            }
        };
        initializer.initialize(DatabaseLoggingEngine.class.getClassLoader());

    }

    @Override
    public String getIdentifier() {
        return DatabaseLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(IngestionPlugin plugin, Execution<Long> execution, MetaDataRecord<Long> mdr,
            String scope, Level level, String... message) {
        TStringDatabaseLogEntry entry = new TStringDatabaseLogEntry();
        entry.setLevel(level);
        entry.setDate(new Date());
        entry.setExecutionId(execution.getId());
        entry.setPluginName(plugin.getName());
        entry.setMetaDataRecordId(mdr.getId());
        entry.setMessage(message);

        storage.getStringHome().update(entry);
    }

    @Override
    public void logStructured(IngestionPlugin plugin, Execution<Long> execution,
            MetaDataRecord<Long> mdr, String scope, Level level, T payload) {
        TObjectDatabaseLogEntry<T> entry = new TObjectDatabaseLogEntry<T>();
        entry.setLevel(level);
        entry.setDate(new Date());
        entry.setExecutionId(execution.getId());
        entry.setPluginName(plugin.getName());
        entry.setMetaDataRecordId(mdr.getId());
        entry.setMessage(payload);

        storage.getObjectHome().update(entry);
    }

    @Override
    public List<LogEntry<Long, String[]>> getExecutionLog(Execution<Long> execution) {
        List<TStringDatabaseLogEntry> entries = storage.getStringHome().findByExecution(
                execution.getId());
        List<LogEntry<Long, String[]>> results = new ArrayList<LogEntry<Long, String[]>>();
        for (TDatabaseLogEntry<?> entry : entries) {
            results.add((TStringDatabaseLogEntry)entry);
        }
        return results;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<LogEntry<Long, T>> getStructuredExecutionLog(Execution<Long> execution) {
        List<TObjectDatabaseLogEntry> entries = storage.getObjectHome().findByExecution(
                execution.getId());
        List<LogEntry<Long, T>> results = new ArrayList<LogEntry<Long, T>>();
        for (TDatabaseLogEntry<?> entry : entries) {
            results.add((LogEntry<Long, T>)entry);
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
        storage.getDurationHome().insert(entries);
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
        storage.getDurationHome().insert(entries);
    }

    @Override
    public Long getAverageDuration(IngestionPlugin plugin) {
        long average = 0;
        List<TDurationDatabaseEntry> entries = storage.getDurationHome().findByPlugin(
                plugin.getName());
        for (TDurationDatabaseEntry entry : entries) {
            average += entry.getDuration();
        }
        average /= entries.size();
        return average;
    }
}
