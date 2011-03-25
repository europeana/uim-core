package eu.europeana.uim.api;

import java.util.Date;

/**
 * Interface for a specific log entry.
 * 
 * @param <T>
 *            message type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 21, 2011
 */
public interface LogEntry<T> {
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
    Object getExecutionId();

    /**
     * @return name of plugin
     */
    String getPluginIdentifier();

    /**
     * @return metadata record ID
     */
    Object getMetaDataRecordId();

    /**
     * @return generic message
     */
    T getMessage();
}
