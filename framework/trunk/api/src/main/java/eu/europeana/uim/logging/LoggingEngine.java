package eu.europeana.uim.logging;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Service for the reporting of the processing, to be used by the orchestrator and plugins
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 21, 2011
 */
// FIXME: simplify logging eninge, backend should take care of using special tables for special
// loggings like link etc.
public interface LoggingEngine<I> {
    /**
     * Gets the identifier of this LoggingEngine implementation
     * 
     * @return a unique identifier for the logging engine
     */
    String getIdentifier();

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Level level, String modul, String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the module which logs this messages
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Level level, Plugin plugin, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Execution<I> execution, Level level, String modul, String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Execution<I> execution, Level level, Plugin plugin, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Level level, String modul, Throwable throwable, String... message);

    /**
     * Logs a message
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param plugin
     *            the plugin where the error occured
     * @param throwable
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Level level, Plugin plugin, Throwable throwable, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable
     *            the throwable causing the error
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            UimDataSet<I> mdr, String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable
     *            the throwable causing the error
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable
     *            the throwable causing the error
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            UimDataSet<I> mdr, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param level
     *            the level of the message
     * @param link
     *            the plain url
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logLink(String modul, String link, int status, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param link
     *            the plain url
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logLink(Execution<I> execution, String modul, UimDataSet<I> mdr, String link, int status,
            String... message);

    /**
     * Logs a message
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param plugin
     *            the module which logs this messages
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param link
     *            the plain url
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logLink(Execution<I> execution, Plugin plugin, UimDataSet<I> mdr, String link, int status,
            String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param level
     *            the level of the message
     * @param field
     *            the content field
     * @param qualifier
     *            the content qualifier
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logField(String modul, String field, String qualifier, int status, String... message);

    /**
     * Logs a message
     * 
     * @param modul
     *            the module which logs this messages
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param field
     *            the content field
     * @param qualifier
     *            the content qualifier
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logField(Execution<I> execution, String modul, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message);

    /**
     * Logs a message
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param plugin
     *            the module which logs this messages
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param field
     *            the content field
     * @param qualifier
     *            the content qualifier
     * @param status
     *            the http status code
     * @param message
     *            message strings
     */
    void logField(Execution<I> execution, Plugin plugin, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message);

    /**
     * Logs a processing duration for a given count of items
     * 
     * @param execution
     *            the execution in which this duration is logged
     * @param module
     *            the module
     * @param duration
     *            duration in ms
     */
    void logDuration(Execution<I> execution, String module, Long duration);

    /**
     * Logs a processing duration for a given count of items
     * 
     * @param execution
     *            the execution in which this duration is logged
     * @param plugin
     *            the plugin
     * @param duration
     *            duration in ms
     */
    void logDuration(Execution<I> execution, Plugin plugin, Long duration);

    /**
     * Logs a EDM validation message (message is related to a particular record)
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param modul
     *            the module which logs this messages
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logEdmCheck(Execution<I> execution, String modul, UimDataSet<I> mdr, String... message);

    /**
     * Logs a EDM validation message (message is related to the whole execution)
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param modul
     *            the module which logs this messages
     * @param mdr
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logEdmCheck(Execution<I> execution, String modul, String... message);

    /**
     * method to ensure flushing to disk at the end of workflows.
     * 
     * @param context
     */
    void completed(ExecutionContext<?, I> context);

    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntry> getLogs(Execution<I> execution);

    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryFailed> getFailedLogs(Execution<I> execution);

    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryLink> getLinkLogs(Execution<I> execution);

    /**
     * @param execution
     * @return the list of edm check log entries for the execution
     */
    List<LogEntryEdmCheck> getEdmCheckLogs(Execution<I> execution);

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 19, 2011
     */
    public interface LogEntry {
        /**
         * @return the log level
         */
        Level getLevel();

        /**
         * @return teh module of occurence
         */
        String getModule();

        /**
         * @return the timeo f occurence
         */
        Date getDate();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();
    }

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 19, 2011
     */
    public interface LogEntryDuration {
        /**
         * @return the module
         */
        String getModule();

        /**
         * @return the date/time of occurrence
         */
        Date getDate();

        /**
         * @return the duration in milliseconds
         */
        Long getDuration();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();
    }

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 19, 2011
     */
    public interface LogEntryFailed {
        /**
         * @return the log level
         */
        Level getLevel();

        /**
         * @return the module
         */
        String getModule();

        /**
         * @return the date of occurrence
         */
        Date getDate();

        /**
         * @return the stacktrace of a possible throwable
         */
        String getStacktrace();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();

        /**
         * @return the data set identifier as string
         */
        String getStringUimDatasetId();
    }

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 19, 2011
     */
    public interface LogEntryLink {
        /**
         * @return the module
         */
        String getModule();

        /**
         * @return the link
         */
        String getLink();

        /**
         * @return the time of occurrence/test
         */
        Date getDate();

        /**
         * @return the http status
         */
        int getStatus();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();

        /**
         * @return the data set identifier as string
         */
        String getStringUimDatasetId();
    }

    /**
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Jul 19, 2011
     */
    public interface LogEntryField {
        /**
         * @return the module
         */
        String getModule();

        /**
         * @return the link
         */
        String getField();

        /**
         * @return the link
         */
        String getQualifier();

        /**
         * @return the time of occurrence/test
         */
        Date getDate();

        /**
         * @return the http status
         */
        int getStatus();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();

        /**
         * @return the data set identifier as string
         */
        String getStringUimDatasetId();
    }

    /**
     * EDM validation messages
     * 
     * @author Nuno Freire (nfreire@gmail.com)
     * @since 4 de Jul de 2013
     */
    public interface LogEntryEdmCheck {
        /**
         * @return teh module of occurence
         */
        String getModule();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();

        /**
         * @return the execution identifier as string
         */
        String getStringMetaDataRecordId();
    }

}