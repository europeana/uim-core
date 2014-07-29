package eu.europeana.uim.logging.modules;

import java.util.Date;

import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Service for the reporting of the processing of field entries, to be used by
 * the orchestrator and plugins
 *
 * @param <I> generic identifier
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface FieldLogging<I> {

    /**
     * Logs a message
     *
     * @param modul the module which logs this messages
     * @param field the content field
     * @param qualifier the content qualifier
     * @param status the http status code
     * @param message message strings
     */
    void logField(String modul, String field, String qualifier, int status, String... message);

    /**
     * Logs a message
     *
     * @param modul the module which logs this messages
     * @param execution the execution during which this log was issues
     * @param mdr the identifier of the metadata record this link belongs to
     * @param field the content field
     * @param qualifier the content qualifier
     * @param status the http status code
     * @param message message strings
     */
    void logField(Execution<I> execution, String modul, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message);

    /**
     * Logs a message
     *
     * @param execution the execution during which this log was issues
     * @param plugin the module which logs this messages
     * @param mdr the identifier of the metadata record this link belongs to
     * @param field the content field
     * @param qualifier the content qualifier
     * @param status the http status code
     * @param message message strings
     */
    void logField(Execution<I> execution, Plugin plugin, UimDataSet<I> mdr, String field,
            String qualifier, int status, String... message);

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
}
