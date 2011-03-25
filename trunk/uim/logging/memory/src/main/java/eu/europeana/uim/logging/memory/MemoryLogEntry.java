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
 * @param <T>
 *            generic message
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MemoryLogEntry<T> implements LogEntry<T> {
    private LoggingEngine.Level level;
    private Date                date;
    private Execution<?>        execution;
    private IngestionPlugin     plugin;
    private MetaDataRecord<?>   mdr;
    private T                   message;

    @Override
    public LoggingEngine.Level getLevel() {
        return level;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Object getExecutionId() {
        return execution.getId();
    }

    @Override
    public String getPluginIdentifier() {
        return plugin.getClass().getSimpleName();
    }

    @Override
    public Object getMetaDataRecordId() {
        return mdr.getId();
    }

    @Override
    public T getMessage() {
        return message;
    }

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
    public MemoryLogEntry(LoggingEngine.Level level, Date date, Execution<?> execution,
                          IngestionPlugin plugin, MetaDataRecord<?> mdr, T message) {
        this.level = level;
        this.date = date;
        this.execution = execution;
        this.plugin = plugin;
        this.mdr = mdr;
        this.message = message;
    }
}
