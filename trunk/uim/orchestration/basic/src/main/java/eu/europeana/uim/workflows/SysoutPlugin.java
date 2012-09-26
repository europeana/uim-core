package eu.europeana.uim.workflows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.UimDataSet;

/**
 * Simple plugin to write to system out.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class SysoutPlugin<U extends UimDataSet<I>, I> extends AbstractIngestionPlugin<U, I> {
    @SuppressWarnings("rawtypes")
    private static TKey<SysoutPlugin, Data> DATA_KEY     = TKey.register(SysoutPlugin.class,
                                                                 "data", Data.class);

    /**
     * ranges of random sleep (e.g. 500-1000, default is 0-100)
     */
    public static final String              SLEEP_RANGES = "sysout.sleep-ranges";

    /**
     * Random sleep enabled?
     */
    public static final String              RANDOM_SLEEP = "sysout.random.sleep";

    /**
     * parameters to be set for topic matching plugin
     */
    private static final List<String>       PARAMETER    = new ArrayList<String>() {
                                                             {
                                                                 add(SLEEP_RANGES);
                                                                 add(RANDOM_SLEEP);
                                                             }
                                                         };

    /**
     * Creates a new instance of this class.
     */
    public SysoutPlugin() {
        super("Console Writer Plugin", "Writes the identifiers of MDRs to sysout.");
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
    public boolean process(U dataset, ExecutionContext<U, I> context)
            throws IngestionPluginFailedException, CorruptedDatasetException {
        Object identifier = dataset.getId();
        Data data = context.getValue(DATA_KEY);

        if (data.randsleep) {
            try {
                int sleep = data.ranges[0] + data.random.nextInt(data.ranges[1]);
                Thread.sleep(sleep);
                System.out.println(getClass().getSimpleName() + ": " + identifier + "(s=" + sleep +
                                   ")");
            } catch (InterruptedException e) {
                // CAUTIOUS, SILENT CATCH
            }
        } else {
            System.out.println(getClass().getSimpleName() + ": " + identifier);
        }

        return true;
    }

    @Override
    public void initialize(ExecutionContext<U, I> context) {
        Data data = new Data();
        context.putValue(DATA_KEY, data);

        String sleep = context.getProperties().getProperty(RANDOM_SLEEP, "false");
        data.randsleep = Boolean.parseBoolean(sleep);

        String ranges = context.getProperties().getProperty(SLEEP_RANGES, "0-100");
        String[] split = ranges.split("-");
        if (split.length == 2) {
            int bottom = Integer.parseInt(split[0]);
            int top = Integer.parseInt(split[1]);
            data.ranges[0] = bottom;
            data.ranges[1] = top;
        }
    }

    @Override
    public void completed(ExecutionContext<U, I> context) {
    }

    @Override
    public List<String> getParameters() {
        return PARAMETER;
    }

    private final static class Data implements Serializable {
        public Random  random    = new Random();
        public boolean randsleep = false;
        public int[]   ranges    = new int[] { 0, 100 };
        @SuppressWarnings("unused")
        public int     processed = 0;
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
