package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

/** Simple logging plugin which logs  
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 25, 2011
 */
public class LoggingPlugin implements IngestionPlugin {

	private static final Logger log = Logger.getLogger(LoggingPlugin.class.getName());
	
    private static TKey<LoggingPlugin, Data> DATA_KEY = TKey.register(LoggingPlugin.class, "data", Data.class);

	/**
	 * Creates a new instance of this class.
	 */
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
	    Data value = context.getValue(DATA_KEY);

		if (value.current++ % value.stepsize == 0) {
			if (log.isLoggable(value.level)) {
				log.log(value.level, "Record:" + mdr.getIdentifier());
			}
		}
	}

    @Override
    public void initialize(ExecutionContext context) throws StorageEngineException {
        Data data = new Data();
        
        Properties properties = context.getProperties();

        String property = properties.getProperty("logging.stepsize","5");
        data.stepsize = Integer.parseInt(property);
     
        property = properties.getProperty("logging.level","FINE");
        data.level = Level.parse(property);
        
        context.putValue(DATA_KEY, data);
    }

    
    @Override
    public void completed(ExecutionContext context) throws StorageEngineException {
    }

    
    @Override
    public List<String> getParameters() {
        return Arrays.asList("logging.stepsize","logging.level");
    }
    
    
    private final static class Data implements Serializable {
        public int current = 0;
        public int stepsize = 5;
        public Level level = Level.FINE;
    }

}
