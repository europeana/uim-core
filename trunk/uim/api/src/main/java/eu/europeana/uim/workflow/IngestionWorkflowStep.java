package eu.europeana.uim.workflow;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;

public class IngestionWorkflowStep extends AbstractWorkflowStep {

	private final IngestionPlugin plugin;
	
	public IngestionWorkflowStep(IngestionPlugin plugin) {
		super(plugin.getIdentifier());
		this.plugin = plugin;
	}
	
	public IngestionWorkflowStep(IngestionPlugin plugin, boolean savepoint) {
		super(plugin.getIdentifier(), savepoint);
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

}
