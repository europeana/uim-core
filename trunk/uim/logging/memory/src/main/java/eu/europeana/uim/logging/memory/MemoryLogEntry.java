package eu.europeana.uim.logging.memory;

import java.util.Date;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MemoryLogEntry<T> implements LogEntry<T> {

    private LoggingEngine.Level level;
    private Date date;
    private Execution execution;
    private IngestionPlugin plugin;
    private MetaDataRecord mdr;
    private T message;

    @Override
    public LoggingEngine.Level getLevel() {
        return level;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Long getExecutionId() {
        return execution.getId();
    }

    @Override
    public String getPluginIdentifier() {
        return plugin.getIdentifier();
    }

    @Override
    public Long getMetaDataRecordId() {
        return mdr.getId();
    }

    @Override
    public T getMessage() {
        return message;
    }

    public MemoryLogEntry(LoggingEngine.Level level, Date date, Execution execution, IngestionPlugin plugin, MetaDataRecord mdr, T message) {
        this.level = level;
        this.date = date;
        this.execution = execution;
        this.plugin = plugin;
        this.mdr = mdr;
        this.message = message;
    }
}
