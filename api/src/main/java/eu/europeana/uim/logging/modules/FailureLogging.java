package eu.europeana.uim.logging.modules;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Service for the reporting of the processing of failed entries, to be used by
 * the orchestrator and plugins
 *
 * @param <I> generic identifier
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface FailureLogging<I> {

    /**
     * Logs a message
     *
     * @param modul the module which logs this messages
     * @param level the level of the message
     * @param throwable the throwable causing the error
     * @param message message strings
     */
    void logFailed(Level level, String modul, Throwable throwable, String... message);

    /**
     * Logs a message
     *
     * @param level the level of the message
     * @param plugin the plugin where the error occured
     * @param throwable the throwable causing the error
     * @param message message strings
     */
    void logFailed(Level level, Plugin plugin, Throwable throwable, String... message);

    /**
     * Logs a message
     *
     * @param modul the module which logs this messages
     * @param execution the execution during which this log was issues
     * @param level the level of the message
     * @param throwable the throwable causing the error
     * @param message message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message);

    /**
     * Logs a message
     *
     * @param modul the module which logs this messages
     * @param execution the execution during which this log was issues
     * @param level the level of the message
     * @param throwable the throwable causing the error
     * @param mdr the identifier of the metadata record this link belongs to
     * @param message message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            UimDataSet<I> mdr, String... message);

    /**
     * Logs a message
     *
     * @param plugin the plugin reporting the log
     * @param execution the execution during which this log was issues
     * @param level the level of the message
     * @param throwable the throwable causing the error
     * @param message message strings
     */
    void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            String... message);

    /**
     * Logs a message
     *
     * @param plugin the plugin reporting the log
     * @param execution the execution during which this log was issues
     * @param level the level of the message
     * @param throwable the throwable causing the error
     * @param mdr the identifier of the metadata record this link belongs to
     * @param message message strings
     */
    void logFailed(Execution<I> execution, Level level, Plugin plugin, Throwable throwable,
            UimDataSet<I> mdr, String... message);

    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryFailed> getFailedLogs(Execution<I> execution);

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
}
