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
    private final LoggingEngine.Level level;
    private final Date                date;
    private final Execution<I>        execution;

    private final String     module;
    private final MetaDataRecord<I>   mdr;
    private final T                   message;
    private final String              scope;

    /**
     * Creates a new instance of this class.
     * 
     * @param module
     * @param execution
     * @param scope
     * @param level
     * @param date
     * @param message
     */
    public MemoryLogEntry(String module, Execution<I> execution, 
                          String scope, LoggingEngine.Level level, Date date, T message) {
        this.scope = scope;
        this.level = level;
        this.date = date;
        this.execution = execution;
        this.module = module;
        this.message = message;
        
        this.mdr = null;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param plugin
     * @param execution
     * @param mdr
     * @param scope
     * @param level
     * @param date
     * @param message
     */
    public MemoryLogEntry(IngestionPlugin plugin, Execution<I> execution, MetaDataRecord<I> mdr,
                          String scope, LoggingEngine.Level level, Date date, T message) {
        this.scope = scope;
        this.level = level;
        this.date = date;
        this.execution = execution;

        this.mdr = mdr;
        this.message = message;
        
        this.module = plugin.getIdentifier();
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
    public String getModule() {
        return module;
    }

    @Override
    public I getMetaDataRecordId() {
        return mdr.getId();
    }

    @Override
    public T getMessage() {
        return message;
    }

    @Override
    public String getScope() {
        return scope;
    }
}
