package eu.europeana.uim.orchestration.basic.workflow;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.UimDataSet;

/**
 * Simple plugin to write to system error.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class SyserrPlugin<U extends UimDataSet<I>, I> extends AbstractIngestionPlugin<U, I> {
    @SuppressWarnings("rawtypes")
    private static TKey<SyserrPlugin, Data> DATA_KEY = TKey.register(SyserrPlugin.class, "data",
                                                             Data.class);

    /**
     * Creates a new instance of this class.
     */
    public SyserrPlugin() {
        super("Error Writer Plugin", "Writes the identifiers of MDRs to sysout.");
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
    public boolean process(U mdr, ExecutionContext<U, I> context)
            throws IngestionPluginFailedException, CorruptedDatasetException {
        Object identifier = mdr.getId();
        Data data = context.getValue(DATA_KEY);

        if (data.randsleep) {
            try {
                int sleep = data.random.nextInt(100);
                Thread.sleep(sleep);
                System.err.println(getClass().getSimpleName() + ": " + identifier + "(s=" + sleep +
                                   ")");
            } catch (InterruptedException e) {
                // CAUTIOUS, SILENT CATCH
            }
        } else {
            System.err.println(getClass().getSimpleName() + ": " + identifier);
        }
        data.processed++;

        if (data.fullfailure) { throw new IngestionPluginFailedException(
                "Failed plugin at record: " + data.processed); }

        if (data.processed % data.errorrate == 0) {
            if (data.corrupted) {
                throw new CorruptedDatasetException("Failed plugin at record: " + data.processed);
            } else {
                throw new NullPointerException("Failed plugin at record: " + data.processed);
            }
        }

        return true;
    }

    @Override
    public void initialize(ExecutionContext<U, I> context) throws IngestionPluginFailedException {
        Data data = new Data();
        context.putValue(DATA_KEY, data);

        String property = context.getProperties().getProperty("syserr.random.sleep", "false");
        data.randsleep = Boolean.parseBoolean(property);

        property = context.getProperties().getProperty("syserr.error.rate", "3");
        data.errorrate = Integer.parseInt(property);

        property = context.getProperties().getProperty("syserr.corrupted", "true");
        data.corrupted = Boolean.parseBoolean(property);

        property = context.getProperties().getProperty("syserr.fullfailure", "false");
        data.fullfailure = Boolean.parseBoolean(property);
    }

    @Override
    public List<String> getParameters() {
        return Arrays.asList("syserr.random.sleep", "syserr.error.rate", "syserr.corrupted",
                "syserr.fullfailure");
    }

    private final static class Data implements Serializable {
        public Random  random      = new Random();
        public boolean randsleep   = false;
        public boolean corrupted   = true;

        public boolean fullfailure = false;
        public int     errorrate   = 3;
        public int     processed   = 0;
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
}
