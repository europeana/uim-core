/* LoggingFacadeEngine.java - created on Oct 24, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.common.MemoryProgressMonitor;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Facade to wrap the logging engine implementations with a writer to stop
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 * @date Oct 24, 2011
 */
public class LoggingFacadeEngine<I> extends MemoryProgressMonitor implements LoggingEngine<I> {

    private Execution<I>              execution;
    private LoggingEngine<I>          delegateLoggingEngine;
    private ExecutionLogFileWriter<I> delegateLogFileWriter;

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * @param delegateLoggingEngine
     * @param delegateLogFileWriter
     */
    public LoggingFacadeEngine(final Execution<I> execution,
                               final LoggingEngine<I> delegateLoggingEngine,
                               final ExecutionLogFileWriter<I> delegateLogFileWriter) {
        this.execution = execution;
        this.delegateLoggingEngine = delegateLoggingEngine;
        this.delegateLogFileWriter = delegateLogFileWriter;
    }

    @Override
    public void worked(int work) {
        super.worked(work);
        
        if (getWorked() % 1000 == 0) {
            try {
                delegateLogFileWriter.log(execution, Level.INFO, "Finished " + getWorked() + " items");
            } catch (IOException e) {
                throw new RuntimeException("Could not write to logfile", e);
            }
        }
    }

    @Override
    public void done() {
        super.done();
        try {
            long period = System.currentTimeMillis() - getStart();
            double persec = getWorked() * 1000.0 / period;
            delegateLogFileWriter.log(execution, Level.INFO, String.format("%d done in %.3f sec. Average %.3f/sec", getWorked(), period / 1000.0, persec));
        } catch (IOException e) {
            throw new RuntimeException("Could not write to logfile", e);
        }
    }


    @Override
    public String getIdentifier() {
        return delegateLoggingEngine.getIdentifier();
    }

    @Override
    public void log(Level level, String modul, String... message) {
        delegateLoggingEngine.log(level, modul, message);
    }

    @Override
    public void log(Level level, IngestionPlugin plugin, String... message) {
        delegateLoggingEngine.log(level, plugin, message);
    }

    @Override
    public void log(Execution<I> execution, Level level, String modul, String... message) {
        if (delegateLogFileWriter != null) {
            for (String msg : message)
                try {
                    delegateLogFileWriter.log(execution, level, modul + ": " + msg);
                } catch (IOException e) {
                    throw new RuntimeException("Error while writing log message", e);
                }
        }
        delegateLoggingEngine.log(execution, level, modul, message);
    }

    @Override
    public void log(Execution<I> execution, Level level, IngestionPlugin plugin, String... message) {
        if (delegateLogFileWriter != null) {
            for (String msg : message)
                try {
                    delegateLogFileWriter.log(execution, level, plugin.getName() + " Plugin: " +
                                                                msg);
                } catch (IOException e) {
                    throw new RuntimeException("Error while writing log message", e);
                }
        }
        delegateLoggingEngine.log(execution, level, plugin, message);
    }

    @Override
    public void logFailed(Level level, String modul, Throwable throwable, String... message) {
        delegateLoggingEngine.logFailed(level, modul, throwable, message);
    }

    @Override
    public void logFailed(Level level, IngestionPlugin plugin, Throwable throwable,
            String... message) {
        delegateLoggingEngine.logFailed(level, plugin, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message) {
        if (delegateLogFileWriter != null) {
            for (String msg : message)
                try {
                    delegateLogFileWriter.log(execution, level, modul + " FAILED: " + msg);
                } catch (IOException e) {
                    throw new RuntimeException("Error while writing log message", e);
                }
        }
        delegateLoggingEngine.logFailed(execution, level, modul, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            MetaDataRecord<I> mdr, String... messages) {

        if (delegateLogFileWriter != null) {
            try {
                if (mdr != null) {
                    delegateLogFileWriter.log(execution, Level.WARNING,
                            "Failed messages for MetadataRecord " + mdr.getId() + "...");
                }
                if (throwable != null) {
                    delegateLogFileWriter.log(execution, Level.WARNING,
                            "Exception: " + throwable.getMessage());
                }
                for (String message : messages)
                    delegateLogFileWriter.log(execution, level, modul + " FAILED: " + message + "");
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }

        delegateLoggingEngine.logFailed(execution, level, modul, throwable, mdr, messages);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, String... message) {
        if (delegateLogFileWriter != null) {
            for (String msg : message)
                try {
                    delegateLogFileWriter.log(execution, level, plugin + " FAILED: " + msg +
                                                                " Execption: " + throwable);
                } catch (IOException e) {
                    throw new RuntimeException("Error while writing log message", e);
                }
        }
        delegateLoggingEngine.logFailed(execution, level, plugin, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, MetaDataRecord<I> mdr, String... messages) {
        if (delegateLogFileWriter != null) {
            try {
                if (mdr != null) {
                    delegateLogFileWriter.log(execution, Level.WARNING,
                            "Failed messages for MetadataRecord " + mdr.getId() + "...");
                }
                if (throwable != null) {
                    delegateLogFileWriter.log(execution, Level.WARNING,
                            "Exception: " + throwable.getMessage());
                }
                for (String message : messages)
                    delegateLogFileWriter.log(execution, level, plugin + " FAILED: " + message + "");
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.logFailed(execution, level, plugin, throwable, mdr, messages);
    }

    @Override
    public void logLink(String modul, String link, int status, String... message) {
        delegateLoggingEngine.logLink(modul, link, status, message);
    }

    @Override
    public void logLink(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String link,
            int status, String... message) {
        delegateLoggingEngine.logLink(execution, modul, mdr, link, status, message);
    }

    @Override
    public void logLink(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String link, int status, String... message) {
        delegateLoggingEngine.logLink(execution, plugin, mdr, link, status, message);
    }

    @Override
    public void logField(String modul, String field, String qualifier, int status,
            String... message) {
        delegateLoggingEngine.logField(modul, field, qualifier, status, message);
    }

    @Override
    public void logField(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String field,
            String qualifier, int status, String... message) {
        delegateLoggingEngine.logField(execution, modul, mdr, field, qualifier, status, message);
    }

    @Override
    public void logField(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String field, String qualifier, int status, String... message) {
        delegateLoggingEngine.logField(execution, plugin, mdr, field, qualifier, status, message);
    }

    @Override
    public void logDuration(Execution<I> execution, String module, Long duration) {
        delegateLoggingEngine.logDuration(execution, module, duration);
    }

    @Override
    public void logDuration(Execution<I> execution, IngestionPlugin plugin, Long duration) {
        delegateLoggingEngine.logDuration(execution, plugin, duration);
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntry<I>> getLogs(Execution<I> execution) {
        return delegateLoggingEngine.getLogs(execution);
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryFailed<I>> getFailedLogs(
            Execution<I> execution) {
        return delegateLoggingEngine.getFailedLogs(execution);
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryLink<I>> getLinkLogs(
            Execution<I> execution) {
        return delegateLoggingEngine.getLinkLogs(execution);
    }

}
