package eu.europeana.uim.workflows;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;

/**
 * Simple plugin to write to system out.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 4, 2011
 */
public class SysoutPlugin implements IngestionPlugin {
    private static TKey<SysoutPlugin, Data> DATA_KEY = TKey.register(SysoutPlugin.class, "data",
                                                             Data.class);

    /**
     * Creates a new instance of this class.
     */
    public SysoutPlugin() {
        // nothing to do
    }

    @Override
    public String getName() {
        return SysoutPlugin.class.getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Writes the identifiers of MDRs to sysout.";
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
    public boolean processRecord(MetaDataRecord mdr, ExecutionContext context)
            throws IngestionPluginFailedException, CorruptedMetadataRecordException {
        String identifier = mdr.getIdentifier();
        Data data = context.getValue(DATA_KEY);

        if (data.randsleep) {
            try {
                int sleep = data.random.nextInt(100);
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
    public void initialize(ExecutionContext context) {
        Data data = new Data();
        context.putValue(DATA_KEY, data);

        String property = context.getProperties().getProperty("sysout.random.sleep", "false");
        data.randsleep = Boolean.parseBoolean(property);
    }

    @Override
    public void completed(ExecutionContext context) {
    }

    @Override
    public List<String> getParameters() {
        return Arrays.asList("sysout.random.sleep");
    }

    private final static class Data implements Serializable {
        public Random  random    = new Random();
        public boolean randsleep = false;
        @SuppressWarnings("unused")
        public int     processed = 0;
    }
}
