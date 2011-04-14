package eu.europeana.uim.logging.mongo;

import java.util.Date;

import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngine.Level;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MongoLogEntry implements LogEntry<Long, String[]> {

    private LoggingEngine.Level level;
    private Date date;
    private Long executionId, mdrId;
    private String pluginIdentifier;
    private String[] message;


    public MongoLogEntry(Date date, Long executionId, LoggingEngine.Level level, Long mdrId, String[] message, String pluginIdentifier) {
        this.date = date;
        this.executionId = executionId;
        this.level = level;
        this.mdrId = mdrId;
        this.message = message;
        this.pluginIdentifier = pluginIdentifier;
    }

    @Override
    public String getPluginName() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public Long getExecutionId() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public Long getMetaDataRecordId() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public String getScope() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public Level getLevel() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public Date getDate() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }


    @Override
    public String[] getMessage() {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }
    
    

}
