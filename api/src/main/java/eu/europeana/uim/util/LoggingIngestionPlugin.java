package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.UimDataSet;

/**
 * Simple logging plugin which logs which {@link MetaDataRecord} is just be
 * processed.
 *
 * @param <U> uim data set type
 * @param <I> generic identifier
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 25, 2011
 */
public class LoggingIngestionPlugin<U extends UimDataSet<I>, I> extends
        AbstractIngestionPlugin<U, I> {

    /**
     * property name to define logging stepsie
     */
    public static final String LOGGING_STEPSIZE = "logging.stepsize";

    /**
     * property name to define logging detail level
     */
    public static final String LOGGING_LEVEL = "logging.level";

    /**
     * parameters to be set for logging plugin
     */
    private static final List<String> PARAMETER = new ArrayList<String>() {
        {
            add(LOGGING_STEPSIZE);
            add(LOGGING_STEPSIZE);
        }
    };

    private static final Logger log = Logger.getLogger(LoggingIngestionPlugin.class.getName());

    @SuppressWarnings("rawtypes")
    private static final TKey<LoggingIngestionPlugin, Data> DATA_KEY = TKey.register(
            LoggingIngestionPlugin.class,
            "data", Data.class);

    /**
     * Creates a new instance of this class.
     */
    public LoggingIngestionPlugin() {
        super("Identifier Logging",
                "Loggs the identifiers of MDRs according the specififed level in the execution (default INFO)");
    }

    @Override
    public TKey<?, ?>[] getInputFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOptionalFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOutputFields() {
        return new TKey[0];
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
    public List<String> getParameters() {
        return PARAMETER;
    }

    @Override
    public boolean process(U mdr, ExecutionContext<U, I> context)
            throws IngestionPluginFailedException, CorruptedDatasetException {
        Data value = context.getValue(DATA_KEY);

        if (value == null) {
            throw new IngestionPluginFailedException(
                    "My internal counting variable '" + DATA_KEY.toString() + "' is null!");
        }

        if (value.current++ % value.stepsize == 0) {
            if (log.isLoggable(value.level)) {
                log.log(value.level, "Record:{0}", mdr.getId());
            }
        }
        return true;
    }

    @Override
    public void initialize(ExecutionContext<U, I> context) throws IngestionPluginFailedException {
        Data data = new Data();

        Properties properties = context.getProperties();

        String property = properties.getProperty("logging.stepsize", "5");
        data.stepsize = Integer.parseInt(property);

        property = properties.getProperty("logging.level", "FINE");
        data.level = Level.parse(property);

        context.putValue(DATA_KEY, data);
    }

    @Override
    public void completed(ExecutionContext<U, I> context) throws IngestionPluginFailedException {
        // nothing to do
    }

    @Override
    public void initialize() {
        // nothing to do
    }

    @Override
    public void shutdown() {
        // nothing to do
    }

    private final static class Data implements Serializable {

        public int current = 0;
        public int stepsize = 5;
        public Level level = Level.FINE;
    }
}
