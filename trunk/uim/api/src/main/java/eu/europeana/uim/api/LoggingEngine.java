package eu.europeana.uim.api;

import java.util.List;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Service for the reporting of the processing, to be used by the orchestrator and eventually
 * plugins
 * 
 * @param <I>
 *            generic identifier
 * @param <T>
 *            message type
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface LoggingEngine<I, T> {
    /**
     * Type of logging information.
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since Mar 21, 2011
     */
    enum Level {
        INFO, WARNING, SEVERE
    }

    /**
     * Gets the identifier of this LoggingEngine implementation
     * 
     * @return a unique identifier for the logging engine
     */
    String getIdentifier();

    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param scope
     *            scope of logging message (further dividing of plugins/processor for example)
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(String modul, Execution<I> execution, String scope,
            Level level, String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param mdr
     *            the record for which this log was issued
     * @param scope
     *            scope of logging message (further dividing of plugins for example)
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(IngestionPlugin plugin, Execution<I> execution, MetaDataRecord<I> mdr, String scope,
            Level level, String... message);

    /**
     * Retrieves simple log entries for one execution
     * 
     * @param execution
     *            the execution
     * @return a list of LogEntry-s
     */
    List<LogEntry<I, String[]>> getExecutionLog(Execution<I> execution);

    /**
     * Logs a structured message, to be used for advanced log analysis
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param mdr
     *            the record for which this log was issued
     * @param scope
     *            scope of logging message (further dividing of plugins for example)
     * @param level
     *            the level of the message
     * @param payload
     *            a structured message object
     */
    void logStructured(IngestionPlugin plugin, Execution<I> execution, MetaDataRecord<I> mdr,
            String scope, Level level, T payload);

    /**
     * Retrieves structured log entries for one execution
     * 
     * @param execution
     *            the execution
     * @return a list of LogEntry-s
     */
    List<LogEntry<I, T>> getStructuredExecutionLog(Execution<I> execution);

    /**
     * Logs a processing duration for a given count of items
     * 
     * @param plugin
     *            the plugin
     * @param duration
     *            duration in ms
     * @param count
     *            the number of items processed during this duration
     */
    void logDuration(IngestionPlugin plugin, Long duration, int count);

    /**
     * Logs a processing duration and attaches information regarding the processed MDRs
     * 
     * @param plugin
     *            the plugin
     * @param duration
     *            duration in ms
     * @param mdr
     *            the identifier of the MDR(s)
     */
    void logDurationDetailed(IngestionPlugin plugin, Long duration, I... mdr);

    /**
     * Gets the average duration of the execution of plugin over a MDR
     * 
     * @param plugin
     *            the plugin
     * @return the average duration in milliseconds
     */
    Long getAverageDuration(IngestionPlugin plugin);
}