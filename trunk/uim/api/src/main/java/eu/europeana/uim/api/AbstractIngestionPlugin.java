package eu.europeana.uim.api;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract ingestion plugin providing defaults and helper methods for plugin implementation.
 * <br/>
 * FIXME we should remove the java.util.Logger and replace it with calls to the UIM Logger
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Manuel Bernhardt (bernhardt.manuel@gmail.com)
 */
public abstract class AbstractIngestionPlugin implements IngestionPlugin {

	private static final Logger log = Logger.getLogger(AbstractIngestionPlugin.class.getName());

	private String qualifier;
	private Level level = Level.FINEST;

	public AbstractIngestionPlugin() {
	}

	public AbstractIngestionPlugin(String qualifier) {
		this.qualifier = qualifier;
	}

	public AbstractIngestionPlugin(String qualifier, Level level) {
		this.qualifier = qualifier;
		this.level = level;
	}

	@Override
	public int getPreferredThreadCount() {
		return 1;
	}

	@Override
	public int getMaximumThreadCount() {
		return 1;
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
		return AbstractIngestionPlugin.class.getSimpleName() + (qualifier != null ? ":" + qualifier : "");
	}


	@Override
	public String getDescription() {
		return "Abstract Ingestion Plugin";
	}


	public abstract void processRecord(MetaDataRecord mdr);

	
	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		String identifier = mdr.getIdentifier();
		if (log.isLoggable(level)) {
			log.log(level, qualifier + ":" + identifier);
		}

        processRecord(mdr, context, this);
	}

    /**
     * This method is the entry point for implementations of this class and should execute the processing of one record in the given context.
     * In order to implement it, use an anonymous inner class as follows:<br>
     *     <pre>
     *     {@code
     *             @Override
     *             public IngestionPluginCall processRecord(ExecutionContext context, MetaDataRecord record, IngestionPlugin plugin) {
     *               return new IngestionPluginCall(context, record, plugin) {
     *                 @Override
     *                 public void processRecord(MetaDataRecord record) {
     *                   log(LoggingEngine.Level.INFO, "Processing record " + record);
     *                 }
     *               };
     *             }
     *     }
     *     </pre>
     *
     * @param context the {@link ExecutionContext} for this plugin invocation
     * @param record the {@link MetaDataRecord} for this plugin invocation
     * @param plugin the {@link IngestionPlugin} for this plugin invocation
     * @return
     */
    public IngestionPluginCall processRecord(MetaDataRecord record, ExecutionContext context, IngestionPlugin plugin) {
    	return new IngestionPluginCall(record, context, plugin) {
    		@Override
			public void processRecord(MetaDataRecord record) {
    			processRecord(record);
    		}
    	};
    }

    
    
    /**
     * Helper class for easing common operations such as logging when implementing an @{IngestionPlugin}
     */
    public abstract static class IngestionPluginCall {
        private final ExecutionContext context;
        private final MetaDataRecord record;
        private final IngestionPlugin plugin;

        public IngestionPluginCall(MetaDataRecord record, ExecutionContext context, IngestionPlugin plugin) {
            this.context = context;
            this.record = record;
            this.plugin = plugin;
        }

        public abstract void processRecord(MetaDataRecord record);

        protected void log(LoggingEngine.Level level, String message) {
            context.getLoggingEngine().log(level, message, context.getExecution(), record, plugin);
        }

        protected <T> void logStructured(LoggingEngine.Level level, T payload) {
            @SuppressWarnings("unchecked")
            LoggingEngine<T> loggingEngine = (LoggingEngine<T>) context.getLoggingEngine();
            loggingEngine.logStructured(level, payload, context.getExecution(), record, plugin);
        }
    }
}
