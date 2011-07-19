/* MemoryLoggingEngine.java - created on Jul 17, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngineAdapter;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * memory based logging engine.
 * 
 * @param <I>
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jul 17, 2011
 */
public class MemoryLoggingEngine<I> implements LoggingEngine<I> {

    private LinkedList<LogEntry>           entries    = new LinkedList<LogEntry>();
    private LinkedList<FailedEntry>        failed     = new LinkedList<FailedEntry>();
    private LinkedList<LinkEntry>          linklogs   = new LinkedList<LinkEntry>();

    private Map<String, SummaryStatistics> durations  = new HashMap<String, SummaryStatistics>();

    private int                            maxentries = 100000;

    /**
     * Creates a new instance of this class.
     */
    public MemoryLoggingEngine() {
    }

    @Override
    public String getIdentifier() {
        return MemoryLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(Level level, String modul, String... message) {
        entries.add(new LogEntry(level, modul, message));
        if (entries.size() > maxentries) {
            entries.removeFirst();
        }
    }

    @Override
    public void log(Level level, IngestionPlugin plugin, String... message) {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public void log(Execution<I> execution, Level level, String modul, String... message) {
        entries.add(new LogEntry(execution, level, modul, message));
        if (entries.size() > maxentries) {
            entries.removeFirst();
        }
    }

    @Override
    public void log(Execution<I> execution, Level level, IngestionPlugin plugin, String... message) {
        log(execution, level, plugin.getIdentifier(), message);
    }

    @Override
    public void logFailed(Level level, String modul, Throwable t, String... message) {
        failed.add(new FailedEntry(level, modul, t, message));
        if (failed.size() > maxentries) {
            failed.removeFirst();
        }
    }

    @Override
    public void logFailed(Level level, IngestionPlugin plugin, Throwable throwable,
            String... message) {
        logFailed(level, plugin.getIdentifier(), throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable t,
            MetaDataRecord<I> mdr, String... message) {
        failed.add(new FailedEntry(execution, level, modul, t, mdr, message));
        if (failed.size() > maxentries) {
            failed.removeFirst();
        }
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin, Throwable t,
            MetaDataRecord<I> mdr, String... message) {
        logFailed(execution, level, plugin.getIdentifier(), t, mdr, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message) {
        failed.add(new FailedEntry(execution, level, modul, throwable, message));
        if (failed.size() > maxentries) {
            failed.removeFirst();
        }
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, String... message) {
        logFailed(execution, level, plugin.getIdentifier(), throwable, message);
    }

    @Override
    public void logLink(String modul, String link, int status, String... message) {
        linklogs.add(new LinkEntry(modul, link, status, message));
        if (entries.size() > maxentries) {
            entries.removeFirst();
        }
    }

    @Override
    public void logLink(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String link,
            int status, String... message) {
        linklogs.add(new LinkEntry(execution, modul, mdr, link, status, message));
        if (entries.size() > maxentries) {
            entries.removeFirst();
        }
    }

    @Override
    public void logLink(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String link, int status, String... message) {
        logLink(execution, plugin.getIdentifier(), mdr, link, status, message);
    }

    @Override
    public void logDuration(Execution<I> execution, String module, Long duration) {
        if (!durations.containsKey(module)) {
            durations.put(module, new SummaryStatistics());
        }

        SummaryStatistics statistics = durations.get(module);
        double value = duration * 1.0;
        statistics.addValue(value);
    }

    @Override
    public void logDuration(Execution<I> execution, IngestionPlugin plugin, Long duration) {
        logDuration(execution, plugin.getIdentifier(), duration);
    }

    @Override
    public List<LoggingEngine.LogEntry<I>> getLogs(Execution<I> execution) {
        List<LoggingEngine.LogEntry<I>> result = new ArrayList<LoggingEngine.LogEntry<I>>();
        for (LogEntry entry : entries) {
            if (entry.execution.equals(execution)) {
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public List<LoggingEngine.LogEntryFailed<I>> getFailedLogs(
            Execution<I> execution) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public List<LoggingEngine.LogEntryLink<I>> getLinkLogs(
            Execution<I> execution) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    
    
    private class LogEntry implements LoggingEngine.LogEntry<I> {
        private final Level        level;
        private final String       module;
        private final String[]     message;

        private final Execution<I> execution;

        public LogEntry(Level level, String module, String[] message) {
            super();
            this.level = level;
            this.module = module;
            this.message = message;
            this.execution = null;
        }

        public LogEntry(Execution<I> execution, Level level, String module, String[] message) {
            super();
            this.execution = execution;
            this.level = level;
            this.module = module;
            this.message = message;
        }

        @Override
        public Level getLevel() {
            return level;
        }

        @Override
        public String getModule() {
            return module;
        }

        @Override
        public String[] getMessages() {
            return message;
        }
    }

    private class FailedEntry implements LoggingEngine.LogEntryFailed<I> {
        private final Level             level;
        private final String            module;
        private final MetaDataRecord<I> mdr;
        private final String[]          message;
        private final Execution<I>      execution;
        private final Throwable         throwable;

        public FailedEntry(Level level, String module, Throwable throwable, String[] message) {
            super();
            this.level = level;
            this.module = module;
            this.mdr = null;
            this.execution = null;
            this.throwable = throwable;
            this.message = message;
        }

        public FailedEntry(Execution<I> execution, Level level, String module, Throwable throwable,
                           String[] message) {
            super();
            this.execution = execution;
            this.level = level;
            this.module = module;
            this.throwable = throwable;
            this.mdr = null;
            this.message = message;
        }

        public FailedEntry(Execution<I> execution, Level level, String module, Throwable throwable,
                           MetaDataRecord<I> mdr, String[] message) {
            super();
            this.execution = execution;
            this.level = level;
            this.module = module;
            this.throwable = throwable;
            this.mdr = mdr;
            this.message = message;
        }

        @Override
        public Level getLevel() {
            return level;
        }

        @Override
        public String getModule() {
            return module;
        }

        @Override
        public String getStacktrace() {
            return LoggingEngineAdapter.getStackTrace(throwable);
        }

        @Override
        public String[] getMessages() {
            return message;
        }

        @Override
        public I getExecution() {
            return execution != null ? execution.getId() : null;
        }

        @Override
        public I getMetaDataRecord() {
            return mdr != null ? mdr.getId() : null;
        }

    }

    private class LinkEntry implements LogEntryLink<I> {
        private String            module;
        private String            link;
        private int               status;
        private String[]          message;

        private MetaDataRecord<I> mdr;
        private Execution<I>      execution;

        public LinkEntry(String modul, String link, int status, String[] message) {
            super();
            this.link = link;
            this.status = status;
            this.message = message;
        }

        public LinkEntry(Execution<I> execution, String module, MetaDataRecord<I> mdr, String link,
                         int status, String[] message) {
            super();
            this.execution = execution;
            this.module = module;
            this.link = link;
            this.mdr = mdr;
            this.status = status;
            this.message = message;
        }

        @Override
        public String getModule() {
            return module;
        }

        @Override
        public String getLink() {
            return link;
        }

        @Override
        public int getStatus() {
            return status;
        }

        @Override
        public String[] getMessages() {
            return message;
        }

        @Override
        public I getExecution() {
            return execution != null ? execution.getId() : null;
        }

        @Override
        public I getMetaDataRecord() {
            return mdr != null ? mdr.getId() : null;
        }

    }

}
