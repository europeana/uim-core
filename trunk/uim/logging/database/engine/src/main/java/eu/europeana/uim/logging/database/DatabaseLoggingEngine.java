package eu.europeana.uim.logging.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngineAdapter;
import eu.europeana.uim.common.BlockingInitializer;
import eu.europeana.uim.logging.database.model.TLogEntry;
import eu.europeana.uim.logging.database.model.TLogEntryDuration;
import eu.europeana.uim.logging.database.model.TLogEntryFailed;
import eu.europeana.uim.logging.database.model.TLogEntryField;
import eu.europeana.uim.logging.database.model.TLogEntryLink;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Database backend of {@link LoggingEngine} using JPA for object relational mapping.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
public class DatabaseLoggingEngine implements LoggingEngine<Long> {
    /** int _500 */
    private static final int        BATCH_SIZE    = 500;

    private static final Logger     log           = Logger.getLogger(DatabaseLoggingEngine.class.getName());

    private DatabaseLoggingStorage  storage;

    private List<TLogEntry>         batchLog      = new LinkedList<TLogEntry>();
    private List<TLogEntryFailed>   batchFailed   = new LinkedList<TLogEntryFailed>();
    private List<TLogEntryField>    batchField    = new LinkedList<TLogEntryField>();
    private List<TLogEntryLink>     batchLink     = new LinkedList<TLogEntryLink>();
    private List<TLogEntryDuration> batchDuration = new LinkedList<TLogEntryDuration>();

    /**
     * Creates a new instance of this class. The default constructor is used to initialize the
     * spring context and alike for the storage engine.
     */
    public DatabaseLoggingEngine() {
        BlockingInitializer initializer = new BlockingInitializer() {
            @Override
            public void initializeInternal() {
                try {
                    status = STATUS_BOOTING;

                    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
                    context.setValidating(false);
                    context.setConfigLocations(new String[] { "/META-INF/db-context.xml",
                            "/META-INF/db-beans.xml" });

                    context.refresh();

                    storage = (DatabaseLoggingStorage)context.getAutowireCapableBeanFactory().autowire(
                            DatabaseLoggingStorage.class,
                            AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
                    status = STATUS_INITIALIZED;
                } catch (Throwable t) {
                    log.log(java.util.logging.Level.SEVERE,
                            "Failed to initialize database logging.", t);
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
    public void log(Level level, String modul, String... messages) {
        TLogEntry entry = new TLogEntry(level, modul, new Date(), messages);
        insert(entry, false);
    }

    @Override
    public void log(Level level, IngestionPlugin plugin, String... messages) {
        log(level, plugin.getIdentifier(), messages);
    }

    @Override
    public void log(Execution<Long> execution, Level level, String modul, String... messages) {
        TLogEntry entry = new TLogEntry(execution.getId(), level, modul, new Date(), messages);
        insert(entry, false);
    }

    @Override
    public void log(Execution<Long> execution, Level level, IngestionPlugin plugin,
            String... messages) {
        TLogEntry entry = new TLogEntry(execution.getId(), level, plugin.getIdentifier(),
                new Date(), messages);
        insert(entry, false);
    }

    @Override
    public void logFailed(Level level, String module, Throwable throwable, String... messages) {
        TLogEntryFailed entry = new TLogEntryFailed(level, module,
                LoggingEngineAdapter.getStackTrace(throwable), new Date(), messages);
        insert(entry, false);
    }

    @Override
    public void logFailed(Level level, IngestionPlugin plugin, Throwable throwable,
            String... messages) {
        logFailed(level, plugin.getIdentifier(), throwable, messages);
    }

    @Override
    public void logFailed(Execution<Long> execution, Level level, String modul,
            Throwable throwable, String... messages) {
        TLogEntryFailed entry = new TLogEntryFailed(execution.getId(), level, modul,
                LoggingEngineAdapter.getStackTrace(throwable), new Date(), messages);
        insert(entry, false);
    }

    @Override
    public void logFailed(Execution<Long> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, String... messages) {
        logFailed(execution, level, plugin.getIdentifier(), throwable, messages);
    }

    @Override
    public void logFailed(Execution<Long> execution, Level level, String modul,
            Throwable throwable, MetaDataRecord<Long> mdr, String... messages) {

        TLogEntryFailed entry = new TLogEntryFailed(execution.getId(), level, modul,
                LoggingEngineAdapter.getStackTrace(throwable), new Date(), mdr.getId(), messages);
        insert(entry, false);
    }

    @Override
    public void logFailed(Execution<Long> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, MetaDataRecord<Long> mdr, String... messages) {
        logFailed(execution, level, plugin.getIdentifier(), throwable, mdr, messages);
    }

    @Override
    public void logLink(String modul, String link, int status, String... messages) {
        TLogEntryLink entry = new TLogEntryLink(modul, link, new Date(), status, messages);
        insert(entry, false);
    }

    @Override
    public void logLink(Execution<Long> execution, String modul, MetaDataRecord<Long> mdr,
            String link, int status, String... messages) {
        TLogEntryLink entry = new TLogEntryLink(execution.getId(), modul, mdr.getId(), link,
                new Date(), status, messages);
        insert(entry, false);
    }

    @Override
    public void logLink(Execution<Long> execution, IngestionPlugin plugin,
            MetaDataRecord<Long> mdr, String link, int status, String... messages) {
        logLink(execution, plugin.getIdentifier(), mdr, link, status, messages);
    }

    @Override
    public void logField(String modul, String field, String qualifier, int status,
            String... messages) {
        TLogEntryField entry = new TLogEntryField(modul, field, qualifier, new Date(), status,
                messages);
        insert(entry, false);
    }

    @Override
    public void logField(Execution<Long> execution, String modul, MetaDataRecord<Long> mdr,
            String field, String qualifier, int status, String... messages) {
        TLogEntryField entry = new TLogEntryField(execution.getId(), modul, mdr.getId(), field,
                qualifier, new Date(), status, messages);
        insert(entry, false);
    }

    @Override
    public void logField(Execution<Long> execution, IngestionPlugin plugin,
            MetaDataRecord<Long> mdr, String field, String qualifier, int status,
            String... messages) {
        logField(execution, plugin.getIdentifier(), mdr, field, qualifier, status, messages);
    }

    @Override
    public void logDuration(Execution<Long> execution, String modul, Long duration) {
        TLogEntryDuration entry = new TLogEntryDuration(modul, new Date(), duration);
        insert(entry, false);
    }

    @Override
    public void logDuration(Execution<Long> execution, IngestionPlugin plugin, Long duration) {
        logDuration(execution, plugin.getIdentifier(), duration);
    }

    @Override
    public List<LoggingEngine.LogEntry<Long>> getLogs(Execution<Long> execution) {
        flush();

        List<LoggingEngine.LogEntry<Long>> result = new ArrayList<LoggingEngine.LogEntry<Long>>();
        List<TLogEntry> entries = storage.getLogHome().findByExecution(execution.getId());
        for (TLogEntry entry : entries) {
            result.add(entry);
        }
        return result;
    }

    @Override
    public List<LoggingEngine.LogEntryFailed<Long>> getFailedLogs(Execution<Long> execution) {
        flush();

        List<LoggingEngine.LogEntryFailed<Long>> result = new ArrayList<LoggingEngine.LogEntryFailed<Long>>();
        List<TLogEntryFailed> entries = storage.getLogFailedHome().findByExecution(
                execution.getId());
        for (TLogEntryFailed entry : entries) {
            result.add(entry);
        }
        return result;
    }

    @Override
    public List<LoggingEngine.LogEntryLink<Long>> getLinkLogs(Execution<Long> execution) {
        flush();

        List<LoggingEngine.LogEntryLink<Long>> result = new ArrayList<LoggingEngine.LogEntryLink<Long>>();
        List<TLogEntryLink> entries = storage.getLogLinkHome().findByExecution(execution.getId());
        for (TLogEntryLink entry : entries) {
            result.add(entry);
        }
        return result;
    }

    private void insert(TLogEntry entry, boolean flush) {
        synchronized (batchLog) {
            if (entry != null) batchLog.add(entry);
            if (batchLog.size() > BATCH_SIZE || flush) {
                storage.getLogHome().insert(batchLog.toArray(new TLogEntry[batchLog.size()]));
                batchLog.clear();
            }
        }
    }

    private void insert(TLogEntryFailed entry, boolean flush) {
        synchronized (batchFailed) {
            if (entry != null) batchFailed.add(entry);
            if (batchFailed.size() > BATCH_SIZE || flush) {
                storage.getLogFailedHome().insert(
                        batchFailed.toArray(new TLogEntryFailed[batchFailed.size()]));
                batchFailed.clear();
            }
        }
    }

    private void insert(TLogEntryField entry, boolean flush) {
        synchronized (batchField) {
            if (entry != null) batchField.add(entry);
            if (batchField.size() > BATCH_SIZE || flush) {
                storage.getLogFieldHome().insert(
                        batchField.toArray(new TLogEntryField[batchField.size()]));
                batchField.clear();
            }
        }
    }

    private void insert(TLogEntryLink entry, boolean flush) {
        synchronized (batchLink) {
            if (entry != null) batchLink.add(entry);
            if (batchLink.size() > BATCH_SIZE || flush) {
                storage.getLogLinkHome().insert(
                        batchLink.toArray(new TLogEntryLink[batchLink.size()]));
                batchLink.clear();
            }
        }
    }

    private void insert(TLogEntryDuration entry, boolean flush) {
        synchronized (batchDuration) {
            if (entry != null) batchDuration.add(entry);
            if (batchDuration.size() > BATCH_SIZE || flush) {
                storage.getLogDurationHome().insert(
                        batchDuration.toArray(new TLogEntryDuration[batchDuration.size()]));
                batchDuration.clear();
            }
        }
    }

    @Override
    public void completed(ExecutionContext<Long> execution) {
        flush();
    }

    private void flush() {
        insert((TLogEntry)null, true);
        insert((TLogEntryFailed)null, true);
        insert((TLogEntryField)null, true);
        insert((TLogEntryLink)null, true);
        insert((TLogEntryDuration)null, true);
    }

}
