/* LoggingEngineAdapter.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Dummy implementation of logging engine.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class LoggingEngineAdapter<I> implements LoggingEngine<I> {
    /** LoggingEngineAdapter for LONG identifiers */
    public static LoggingEngine<Long> LONG   = new LoggingEngineAdapter<Long>() {
                                             };
    /** LoggingEngineAdapter for STRING identifiers */
    public static LoggingEngine<Long> STRING = new LoggingEngineAdapter<Long>() {
                                             };

    @Override
    public String getIdentifier() {
        return LoggingEngineAdapter.class.getSimpleName();
    }

    @Override
    public void log(Level level, String modul, String... message) {
    }

    @Override
    public void log(Level level, IngestionPlugin plugin, String... message) {
    }

    @Override
    public void log(Execution<I> execution, Level level, String modul, String... message) {
    }

    @Override
    public void log(Execution<I> execution, Level level, IngestionPlugin plugin, String... message) {
    }

    @Override
    public void logFailed(Level level, String modul, Throwable throwable, String... message) {
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            MetaDataRecord<I> mdr, String... message) {
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, MetaDataRecord<I> mdr, String... message) {
    }

    @Override
    public void logFailed(Level level, IngestionPlugin plugin, Throwable throwable,
            String... message) {
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message) {
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, String... message) {
    }

    @Override
    public void logLink(String modul, String link, int status, String... message) {
    }

    @Override
    public void logLink(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String link,
            int status, String... message) {
    }

    @Override
    public void logLink(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String link, int status, String... message) {
    }

    @Override
    public void logField(String modul, String field, String qualifier, int status,
            String... message) {
    }

    @Override
    public void logField(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String field,
            String qualifier, int status, String... message) {
    }

    @Override
    public void logField(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String field, String qualifier, int status, String... message) {
    }

    @Override
    public void logDuration(Execution<I> execution, String module, Long duration) {
    }

    @Override
    public void logDuration(Execution<I> execution, IngestionPlugin plugin, Long duration) {
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntry<I>> getLogs(Execution<I> execution) {
        return Collections.emptyList();
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryFailed<I>> getFailedLogs(
            Execution<I> execution) {
        return Collections.emptyList();
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryLink<I>> getLinkLogs(
            Execution<I> execution) {
        return Collections.emptyList();
    }

    /**
     * utility method to serialize a throwable to a string
     * 
     * @param throwable
     * @return the whole stacktrace in string representation
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }
}
