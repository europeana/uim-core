package eu.europeana.uim.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.store.Execution;

public class LoggingPlugin implements IngestionPlugin {

	private static final Logger log = Logger.getLogger(LoggingPlugin.class.getName());

	private Level level = Level.FINE;
	private int stepsize = 5;

	public LoggingPlugin() {
	}

	public LoggingPlugin(int stepsize) {
		this.stepsize = stepsize;
	}

	public LoggingPlugin(Level level, int stepsize) {
		this.level = level;
		this.stepsize = stepsize;
	}

	@Override
	public int getPreferredThreadCount() {
		return 5;
	}

	@Override
	public int getMaximumThreadCount() {
		return 10;
	}


	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getInputParameters() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getOutputParameters() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getTransientParameters() {
		return new TKey[0];
	}


	@Override
	public String getIdentifier() {
		return LoggingPlugin.class.getSimpleName();
	}


	@Override
	public String getDescription() {
		return "Logges the identifiers of MDRs according the specififed level:" + level.toString();
	}

	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		int current = 1;

		Integer value = (Integer) context.getValue(this, "count");
		if (value != null) {
			current = value.intValue() + 1;
		} else {
			context.putValue(this, "count", current);
		}

		if (current % stepsize == 0) {
			if (log.isLoggable(level)) {
				log.log(level, "Record:" + mdr.getIdentifier());
			}
		}
	}

}
