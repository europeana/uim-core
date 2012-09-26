/* LoggingFacadeEngine.java - created on Oct 24, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import eu.europeana.uim.common.progress.MemoryProgressMonitor;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Facade to wrap the logging engine implementations with a writer to stop
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 * @date Oct 24, 2011
 */
public class LoggingFacadeEngine<I> extends MemoryProgressMonitor implements LoggingEngine<I> {
    private final Execution<I>                         execution;
    private final UimDataSet<I>                        dataset;
    private final Workflow<? extends UimDataSet<I>, I> workflow;
    private final Properties                           properties;

    private final LoggingEngine<I>                     delegateLoggingEngine;
    private final ExecutionLogFileWriter<I>            delegateLogFileWriter;

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * @param dataset
     * @param workflow
     * @param properties
     * @param delegateLoggingEngine
     * @param delegateLogFileWriter
     */
    public LoggingFacadeEngine(Execution<I> execution, UimDataSet<I> dataset,
                               Workflow<? extends UimDataSet<I>, I> workflow,
                               Properties properties, LoggingEngine<I> delegateLoggingEngine,
                               ExecutionLogFileWriter<I> delegateLogFileWriter) {
        this.execution = execution;
        this.dataset = dataset;
        this.workflow = workflow;
        this.properties = properties;
        this.delegateLoggingEngine = delegateLoggingEngine;
        this.delegateLogFileWriter = delegateLogFileWriter;
    }

    @Override
    public void beginTask(String name, int work) {
        try {
            super.beginTask(name, work);

            delegateLogFileWriter.log(execution, Level.INFO,
                    "Start Workflow:" + workflow.getName() + " on " + dataset.toString());
            delegateLogFileWriter.log(execution, Level.INFO, "Command:" + generateCommandLine());
        } catch (IOException e) {
            throw new RuntimeException("Could not write to logfile", e);
        }
    }

    @Override
    public void worked(int work) {
        super.worked(work);
        if (getWorked() % 10000 == 0) {
            try {
                long period = System.currentTimeMillis() - getStart();
                double persec = getWorked() * 1000.0 / period;
                delegateLogFileWriter.log(
                        execution,
                        Level.INFO,
                        "Done " +
                                getWorked() +
                                String.format(" records in %.3f sec. Average %.3f/sec",
                                        period / 1000.0, persec));
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
            delegateLogFileWriter.log(execution, Level.INFO, String.format(
                    "%d done in %.3f sec. Average %.3f/sec", getWorked(), period / 1000.0, persec));
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
        String joinedMessage = StringUtils.join(message, "\n");
        try {
            delegateLogFileWriter.log(execution, level, modul + " modul:" + joinedMessage);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing log message", e);
        }

        delegateLoggingEngine.log(level, modul, message);
    }

    @Override
    public void log(Level level, Plugin plugin, String... message) {
        String joinedMessage = StringUtils.join(message, "\n");
        try {
            delegateLogFileWriter.log(execution, level, joinedMessage);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing log message", e);
        }
        delegateLoggingEngine.log(level, plugin, message);
    }

    @Override
    public void log(Execution<I> execution, Level level, String modul, String... message) {
        if (delegateLogFileWriter != null) {

            String joinedMessage = StringUtils.join(message, "\n");
            try {
                delegateLogFileWriter.log(execution, level, modul + ": " + joinedMessage);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.log(execution, level, modul, message);
    }

    @Override
    public void log(Execution<I> execution, Level level, Plugin plugin, String... message) {
        if (delegateLogFileWriter != null) {
            String joinedMessage = StringUtils.join(message, "\n");
            try {
                delegateLogFileWriter.log(execution, level, plugin.getName() + " Plugin: " +
                                                            joinedMessage);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.log(execution, level, plugin, message);
    }

    @Override
    public void logFailed(Level level, String modul, Throwable throwable, String... message) {
        if (delegateLogFileWriter != null) {
            String joinedMessage = StringUtils.join(message, "\n");
            String stackTrace = generateStacktrace(throwable);
            try {
                delegateLogFileWriter.log(execution, level, modul + " modul: " + joinedMessage +
                                                            "\n" + stackTrace);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }

        delegateLoggingEngine.logFailed(level, modul, throwable, message);
    }

    @Override
    public void logFailed(Level level, Plugin plugin, Throwable throwable, String... message) {
        if (delegateLogFileWriter != null) {
            String joinedMessage = StringUtils.join(message, "\n");
            String stackTrace = generateStacktrace(throwable);
            try {
                delegateLogFileWriter.log(execution, level, plugin + " : " + joinedMessage + "\n" +
                                                            stackTrace);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.logFailed(level, plugin, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message) {
        if (delegateLogFileWriter != null) {
            String joinedMessage = StringUtils.join(message, "\n");

            try {
                delegateLogFileWriter.log(execution, level, modul + " FAILED: " + joinedMessage);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.logFailed(execution, level, modul, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            UimDataSet<I> mdr, String... messages) {

        if (delegateLogFileWriter != null) {
            try {
                String generateStacktrace = generateStacktrace(throwable);
                String joinedMessage = StringUtils.join(messages, "\n");
                delegateLogFileWriter.log(execution, level,
                        "Failed messages for UimDataSet " + mdr.getId() + ", " + modul +
                                " FAILED: " + joinedMessage + "\n" + generateStacktrace);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }

        delegateLoggingEngine.logFailed(execution, level, modul, throwable, mdr, messages);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            String... message) {
        if (delegateLogFileWriter != null) {
            try {
                String generateStacktrace = generateStacktrace(throwable);
                String joinedMessage = StringUtils.join(message, "\n");
                delegateLogFileWriter.log(execution, level, plugin.getName() + " FAILED: " +
                                                            joinedMessage + "\n" +
                                                            generateStacktrace);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.logFailed(execution, level, plugin, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            UimDataSet<I> mdr, String... message) {
        if (delegateLogFileWriter != null) {
            try {
                String generateStacktrace = generateStacktrace(throwable);
                String joinedMessage = StringUtils.join(message, "\n");
                delegateLogFileWriter.log(execution, level,
                        "Failed messages for UimDataSet " + mdr.getId() + ", " + plugin.getName() +
                                " FAILED: " + joinedMessage + "\n" + generateStacktrace);
            } catch (IOException e) {
                throw new RuntimeException("Error while writing log message", e);
            }
        }
        delegateLoggingEngine.logFailed(execution, level, plugin, throwable, mdr, message);
    }

    @Override
    public void logLink(String modul, String link, int status, String... message) {
        delegateLoggingEngine.logLink(modul, link, status, message);
    }

    @Override
    public void logLink(Execution<I> execution, String modul, UimDataSet<I> mdr, String link,
            int status, String... message) {
        delegateLoggingEngine.logLink(execution, modul, mdr, link, status, message);
    }

    @Override
    public void logLink(Execution<I> execution, Plugin plugin, UimDataSet<I> mdr, String link,
            int status, String... message) {
        delegateLoggingEngine.logLink(execution, plugin, mdr, link, status, message);
    }

    @Override
    public void logField(String modul, String field, String qualifier, int status,
            String... message) {

        delegateLoggingEngine.logField(modul, field, qualifier, status, message);
    }

    @Override
    public void logField(Execution<I> execution, String modul, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message) {
        delegateLoggingEngine.logField(execution, modul, mdr, field, qualifier, status, message);
    }

    @Override
    public void logField(Execution<I> execution, Plugin plugin, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message) {
        delegateLoggingEngine.logField(execution, plugin, mdr, field, qualifier, status, message);
    }

    @Override
    public void logDuration(Execution<I> execution, String module, Long duration) {
        delegateLoggingEngine.logDuration(execution, module, duration);
    }

    @Override
    public void logDuration(Execution<I> execution, Plugin plugin, Long duration) {
        delegateLoggingEngine.logDuration(execution, plugin, duration);
    }

    @Override
    public List<eu.europeana.uim.logging.LoggingEngine.LogEntry> getLogs(Execution<I> execution) {
        return delegateLoggingEngine.getLogs(execution);
    }

    @Override
    public List<eu.europeana.uim.logging.LoggingEngine.LogEntryFailed> getFailedLogs(
            Execution<I> execution) {
        return delegateLoggingEngine.getFailedLogs(execution);
    }

    @Override
    public List<eu.europeana.uim.logging.LoggingEngine.LogEntryLink> getLinkLogs(
            Execution<I> execution) {
        return delegateLoggingEngine.getLinkLogs(execution);
    }

    @Override
    public void completed(ExecutionContext<?, I> execution) {
        delegateLoggingEngine.completed(execution);
    }

    private String generateStacktrace(Throwable t) {
        if (t == null) { return ""; }
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @SuppressWarnings("rawtypes")
    private String generateCommandLine() {
        StringBuilder b = new StringBuilder();
        b.append("uim:exec -o start ");
        b.append(workflow.getIdentifier());
        b.append(" ");

        if (dataset instanceof MetaDataRecord) {
            b.append(((MetaDataRecord)dataset).getId());
        } else if (dataset instanceof Collection) {
            b.append(((Collection)dataset).getMnemonic());
        } else if (dataset instanceof Provider) {
            b.append(((Provider)dataset).getMnemonic());
        }

        if (properties != null && !properties.isEmpty()) {

            StringBuilder p = new StringBuilder();
            for (Object key : properties.keySet()) {
                String value = properties.getProperty((String)key);
                if (value != null && !value.isEmpty()) {
                    if (p.length() > 0) {
                        p.append("&");
                    }
                    p.append(key);
                    p.append("=");
                    p.append(value);
                }
            }
            b.append(" ");
            b.append(p.toString());
        }

        return b.toString();
    }
}
