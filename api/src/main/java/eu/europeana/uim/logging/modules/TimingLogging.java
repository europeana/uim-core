package eu.europeana.uim.logging.modules;

import java.util.Date;

import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;

/**
 * Service for the reporting of the processing of timing loggings, to be used by
 * the orchestrator and plugins
 *
 * @param <I> generic identifier
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface TimingLogging<I> {

    /**
     * Logs a processing duration for a given count of items
     *
     * @param execution the execution in which this duration is logged
     * @param module the module
     * @param duration duration in ms
     */
    void logDuration(Execution<I> execution, String module, Long duration);

    /**
     * Logs a processing duration for a given count of items
     *
     * @param execution the execution in which this duration is logged
     * @param plugin the plugin
     * @param duration duration in ms
     */
    void logDuration(Execution<I> execution, Plugin plugin, Long duration);

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
}
