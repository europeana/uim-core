package eu.europeana.uim.logging.database;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

public class DatabaseLoggingEngine<T extends Serializable> implements LoggingEngine<T> {

    private DatabaseLogEntryHome home;

    public DatabaseLoggingEngine() {
    }

    @Override
    public String getIdentifier() {
        return DatabaseLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(Level level, String message, Execution<?> execution, MetaDataRecord<?> mdr,
            IngestionPlugin plugin) {
        DatabaseLogEntry entry = new DatabaseLogEntry();
        entry.setLevel(level.toString());
        entry.setTime(new Date());
        entry.setStarttime(execution.getStartTime());
        entry.setEndtime(execution.getEndTime());
        entry.setPlugin(plugin.getName());
        entry.setWorkflow(execution.getWorkflowName());
        entry.setDataset(execution.getDataSet().getId());
        entry.setRecord("" + mdr.getId());
        entry.setDescription(message);

        home.update(entry);
    }

    @Override
    public void logStructured(Level level, T payload, Execution<?> execution,
            MetaDataRecord<?> mdr, IngestionPlugin plugin) {
        DatabaseLogEntry entry = new DatabaseLogEntry();
        entry.setLevel(level.toString());
        entry.setTime(new Date());
        entry.setStarttime(execution.getStartTime());
        entry.setEndtime(execution.getEndTime());
        entry.setPlugin(plugin.getName());
        entry.setWorkflow(execution.getWorkflowName());
        entry.setDataset(execution.getDataSet().getId());
        entry.setRecord("" + mdr.getId());
        entry.setDescription(payload.toString());

        home.update(entry);
    }

    @Override
    public void logDuration(IngestionPlugin plugin, Long duration, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public Long getAverageDuration(IngestionPlugin plugin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LogEntry<String>> getExecutionLog(Execution<?> execution) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public List<LogEntry<T>> getStructuredExecutionLog(Execution<?> execution) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public void logDurationDetailed(IngestionPlugin plugin, Long duration, long... mdr) {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }
}
