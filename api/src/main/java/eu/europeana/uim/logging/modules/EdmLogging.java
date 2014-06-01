package eu.europeana.uim.logging.modules;

import java.util.List;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Service for the reporting of the processing used solely in case of edm
 * validation.
 *
 * @param <I> generic identifier
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface EdmLogging<I> {

    /**
     * Logs a EDM validation message (message is related to a particular record)
     *
     * @param execution the execution during which this log was issues
     * @param modul the module which logs this messages
     * @param mdr the identifier of the metadata record this link belongs to
     * @param message message strings
     */
    void logEdmCheck(Execution<I> execution, String modul, UimDataSet<I> mdr, String... message);

    /**
     * Logs a EDM validation message (message is related to the whole execution)
     *
     * @param execution the execution during which this log was issues
     * @param modul the module which logs this messages
     * @param message message strings
     */
    void logEdmCheck(Execution<I> execution, String modul, String... message);

    /**
     * @param execution
     * @return the list of edm check log entries for the execution
     */
    List<LogEntryEdmCheck> getEdmCheckLogs(Execution<I> execution);

    /**
     * EDM validation messages
     *
     * @author Nuno Freire (nfreire@gmail.com)
     * @since 4 de Jul de 2013
     */
    public interface LogEntryEdmCheck {

        /**
         * @return teh module of occurence
         */
        String getModule();

        /**
         * @return the messages
         */
        String[] getMessages();

        /**
         * @return the execution identifier as string
         */
        String getStringExecutionId();

        /**
         * @return the execution identifier as string
         */
        String getStringMetaDataRecordId();
    }
}
