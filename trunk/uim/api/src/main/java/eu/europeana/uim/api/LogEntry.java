package eu.europeana.uim.api;

import java.util.Date;

import eu.europeana.uim.api.LoggingEngine.Level;

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
     * @return name of plugin
     */
    String getPluginName();

    /**
     * @return for which execution
     */
    I getExecutionId();

    /**
     * @return metadata record ID
     */
    I getMetaDataRecordId();

    /**
     * @return scope of logging message (further dividing of plugins for example)
     */
    String getScope();

    /**
     * @return level of logging
     */
    Level getLevel();

    /**
     * @return date of creation
     */
    Date getDate();

    /**
     * @return generic message
     */
    T getMessage();
}
