package eu.europeana.uim.workflow;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

public class IngestionWorkflowStep extends AbstractWorkflowStep {

	private final IngestionPlugin plugin;
	
	public IngestionWorkflowStep(IngestionPlugin plugin) {
		super(plugin.getClass().getSimpleName());
		this.plugin = plugin;
	}
	
	public IngestionWorkflowStep(IngestionPlugin plugin, boolean savepoint) {
		super(plugin.getClass().getSimpleName(), savepoint);
		this.plugin = plugin;
	}
	
	@Override
	public int getPreferredThreadCount() {
		return plugin.getPreferredThreadCount();
	}

	@Override
	public int getMaximumThreadCount() {
		return plugin.getMaximumThreadCount();
	}

	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		plugin.processRecord(mdr, context);
	}


    @Override
    public <T> void initialize(ActiveExecution<T> visitor)  throws StorageEngineException {
        plugin.initialize(visitor);
    }


    @Override
    public <T> void finalize(ActiveExecution<T> visitor)  throws StorageEngineException {
        plugin.finalize(visitor);
    }

}
