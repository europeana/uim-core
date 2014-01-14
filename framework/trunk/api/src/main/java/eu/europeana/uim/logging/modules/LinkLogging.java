package eu.europeana.uim.logging.modules;

import java.util.Date;
import java.util.List;

import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Service for the reporting of the processing of links, to be used by the orchestrator and plugins
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface LinkLogging<I> {
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
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryLink> getLinkLogs(Execution<I> execution);

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
}