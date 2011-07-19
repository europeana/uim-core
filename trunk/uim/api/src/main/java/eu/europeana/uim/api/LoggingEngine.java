package eu.europeana.uim.api;

import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Service for the reporting of the processing, to be used by the orchestrator and
 * plugins
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 21, 2011
 */
public interface LoggingEngine<I> {

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
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Level level, String modul, String... message);

    /**
     * Logs a message
     * @param plugin 
     *              the module which logs this messages
     * 
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Level level, IngestionPlugin plugin, String... message);

    
    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param message
     *            message strings
     */
    void log(Execution<I> execution, Level level, String modul, 
             String... message);

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
    void log(Execution<I> execution, Level level, IngestionPlugin plugin, 
            String... message);



    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable 
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Level level, String modul, Throwable throwable, String... message);



    /**
     * Logs a message
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param plugin 
     *            the plugin where the error occured
     * @param throwable 
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Level level, IngestionPlugin plugin, Throwable throwable, String... message);


    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable 
     *            the throwable causing the error
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable, 
             String... message);


    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable 
     *            the throwable causing the error
     * @param mdr 
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable, 
            MetaDataRecord<I> mdr, String... message);


    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable 
     *            the throwable causing the error
     * @param mdr 
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin, Throwable throwable, 
            String... message);

    /**
     * Logs a message
     * 
     * @param plugin
     *            the plugin reporting the log
     * @param execution
     *            the execution during which this log was issues
     * @param level
     *            the level of the message
     * @param throwable 
     *            the throwable causing the error
     * @param mdr 
     *            the identifier of the metadata record this link belongs to
     * @param message
     *            message strings
     */
    void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin, Throwable throwable, 
            MetaDataRecord<I> mdr, String... message);


    /**
     * Logs a message
     * @param modul 
     *              the module which logs this messages
     * 
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
     * @param modul 
     *              the module which logs this messages
     * 
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
    void logLink(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String link,  
             int status, String... message);

    /**
     * Logs a message
     * 
     * @param execution
     *            the execution during which this log was issues
     * @param plugin 
     *              the module which logs this messages
     * @param mdr 
     *            the identifier of the metadata record this link belongs to
     * @param link
     *            the plain url  
     * @param status 
     *            the http status code
     * @param message
     *            message strings
     */
    void logLink(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr, String link,  
             int status, String... message);


    /**
     * Logs a processing duration for a given count of items
     * @param execution 
     *            the execution in which this duration is logged
     * @param module
     *            the module
     * @param duration
     *            duration in ms
     */
    void logDuration(Execution<I>  execution, String module, Long duration);

    
    /**
     * Logs a processing duration for a given count of items
     * @param execution 
     *            the execution in which this duration is logged
     * @param plugin
     *            the plugin
     * @param duration
     *            duration in ms
     */
    void logDuration(Execution<I> execution, IngestionPlugin plugin, Long duration);

    
    
    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntry<I>> getLogs(Execution<I> execution);

    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryFailed<I>> getFailedLogs(Execution<I> execution);

    
    
    /**
     * @param execution
     * @return the list of failed log entries for the execution
     */
    List<LogEntryLink<I>> getLinkLogs(Execution<I> execution);
    
    public interface LogEntry<I> {
        Level getLevel();
        String getModule();
        String[] getMessages();
    }
    
    public interface LogEntryDuration<I> {
        String getModule();
        Long getDuration();

        I getExecution();
    }
    
    public interface LogEntryFailed<I> {
        Level getLevel();
        String getModule();
        String getStacktrace();
        String[] getMessages();
        
        I getExecution();
        I getMetaDataRecord();
    }
    
    public interface LogEntryLink<I> {
        String getModule();
        String getLink();
        int getStatus();
        String[] getMessages();
        
        I getExecution();
        I getMetaDataRecord();
    }
    
}