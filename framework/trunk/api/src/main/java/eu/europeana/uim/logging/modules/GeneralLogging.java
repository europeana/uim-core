package eu.europeana.uim.logging.modules;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;

/**
 * Service for the reporting of the processing of general log messages, to be used by the
 * orchestrator and plugins
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface GeneralLogging<I> {
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
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntry> getLogs(Execution<I> execution);

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
}