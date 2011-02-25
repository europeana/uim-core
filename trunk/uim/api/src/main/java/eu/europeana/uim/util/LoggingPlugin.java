package eu.europeana.uim.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Execution;

/** Simple logging plugin which logs  
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 25, 2011
 */
public class LoggingPlugin implements IngestionPlugin {

	private static final Logger log = Logger.getLogger(LoggingPlugin.class.getName());

	public LoggingPlugin() {
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
	public TKey<MDRFieldRegistry, ?>[] getInputFields() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
		return new TKey[0];
	}

	@Override
	public String getDescription() {
		return "Logges the identifiers of MDRs according the specififed level in the execution (default INFO)";
	}

	
	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		int current = 1;
		
		int stepsize = 5;
		if (context.hasValue(this, "logging.stepsize")){
		    stepsize = (Integer)context.getValue(this, "logging.stepsize");
		}
		
		Level level = Level.FINE;
		if (context.hasValue(this, "logging.level")) {
		    level = (Level)context.getValue(this, "logging.level");
		}
		

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

    @Override
    public <T> void initialize(ExecutionContext context) throws StorageEngineException {
        Properties properties = context.getProperties();
        String property = properties.getProperty("logging.stepsize","5");
        context.putValue(this, "logging.size", Integer.parseInt(property));
     
        property = properties.getProperty("logging.level","FINE");
        context.putValue(this, "logging.level", Level.parse(property));
    }

    @Override
    public <T> void finalize(ExecutionContext context) throws StorageEngineException {
    }

    @Override
    public List<String> getParameters() {
        return Arrays.asList("logging.stepsize","logging.level");
    }
}
