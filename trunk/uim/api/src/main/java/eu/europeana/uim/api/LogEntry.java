package eu.europeana.uim.api;

import java.util.Date;

/**
 * Interface for a specific log entry.
 * 
 * @param <I>
 *            generic identifier
 * @param <T>
 *            message type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface LogEntry<I, T> {
    /**
     * @return level of logging
     */
    LoggingEngine.Level getLevel();

    /**
     * @return date of creation
     */
    Date getDate();

    /**
     * @return for which execution
     */
    I getExecutionId();

    /**
     * @return name of plugin
     */
    String getPluginName();

    /**
     * @return metadata record ID
     */
    I getMetaDataRecordId();

    /**
     * @return generic message
     */
    T getMessage();
}
