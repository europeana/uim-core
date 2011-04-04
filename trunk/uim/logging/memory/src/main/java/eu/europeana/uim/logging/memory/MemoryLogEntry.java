package eu.europeana.uim.logging.memory;

import java.util.Date;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

/**
 * Implementation of an in-memory log entry.
 * 
 * @param <I>
 *            generic identifier
 * @param <T>
 *            generic message
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MemoryLogEntry<I, T> implements LogEntry<I, T> {
    private LoggingEngine.Level level;
    private Date                date;
    private Execution<I>        execution;
    private IngestionPlugin     plugin;
    private MetaDataRecord<I>   mdr;
    private T                   message;

    /**
     * Creates a new instance of this class.
     * 
     * @param level
     * @param date
     * @param execution
     * @param plugin
     * @param mdr
     * @param message
     */
    public MemoryLogEntry(LoggingEngine.Level level, Date date, Execution<I> execution,
                          IngestionPlugin plugin, MetaDataRecord<I> mdr, T message) {
        this.level = level;
        this.date = date;
        this.execution = execution;
        this.plugin = plugin;
        this.mdr = mdr;
        this.message = message;
    }
    
    @Override
    public LoggingEngine.Level getLevel() {
        return level;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public I getExecutionId() {
        return execution.getId();
    }

    @Override
    public String getPluginIdentifier() {
        return plugin.getClass().getSimpleName();
    }

    @Override
    public I getMetaDataRecordId() {
        return mdr.getId();
    }

    @Override
    public T getMessage() {
        return message;
    }
}
