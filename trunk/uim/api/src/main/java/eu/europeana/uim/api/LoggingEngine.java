package eu.europeana.uim.api;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Execution;

import java.util.List;

/**
 * Service for the reporting of the processing, to be used by the orchestrator and eventually plugins
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface LoggingEngine<T> {

    enum Level {
        INFO, WARNING, SEVERE
    }

    /**
     * Gets the identifier of this LoggingEngine implementation
     * @return a unique identifier for the logging engine
     */
    String getIdentifier();

    /**
     * Logs a message
     *
     * @param level     the level of the message
     * @param message   the message string
     * @param execution the execution during which this log was issues
     * @param mdr       the record for which this log was issued
     * @param plugin    the plugin reporting the log
     */
    void log(Level level, String message, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin);

    /**
     * Retrieves simple log entries for one execution
     *
     * @param execution the execution
     * @return a list of LogEntry-s
     */
    List<LogEntry<String>> getExecutionLog(Execution execution);

    /**
     * Logs a structured message, to be used for advanced log analysis
     *
     * @param level     the level of the message
     * @param payload   a structured message object
     * @param execution the execution during which this log was issues
     * @param mdr       the record for which this log was issued
     * @param plugin    the plugin reporting the log
     */
    void logStructured(Level level, T payload, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin);

    /**
     * Retrieves structured log entries for one execution
     * @param execution the execution
     * @return a list of LogEntry-s
     */
    List<LogEntry<T>> getStructuredExecutionLog(Execution execution);

    /**
     * Logs a processing duration for a given count of items
     *
     * @param plugin   the plugin
     * @param duration duration in ms
     * @param count    the number of items processed during this duration
     */
    void logDuration(IngestionPlugin plugin, Long duration, int count);

    /**
     * Gets the average duration of the execution of plugin over a MDR
     *
     * @param plugin the plugin
     * @return the average duration in milliseconds
     */
    Long getAverageDuration(IngestionPlugin plugin);

    /**
     * Logs a processing duration and attaches information regarding the processed MDRs
     *
     * @param plugin   the plugin
     * @param duration duration in ms
     * @param mdr      the identifier of the MDR(s)
     */
    void logDuration(IngestionPlugin plugin, Long duration, long... mdr);


}