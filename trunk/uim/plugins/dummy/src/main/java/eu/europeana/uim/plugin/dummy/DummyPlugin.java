package eu.europeana.uim.plugin.dummy;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;

/**
 * Dummy plugin only for logging.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 4, 2011
 */
public class DummyPlugin implements IngestionPlugin {
    private static Logger log     = Logger.getLogger(DummyPlugin.class.getName());

    private static int    counter = 0;

    /**
     * Creates a new instance of this class.
     */
    public DummyPlugin() {
        // nothing to do
    }

    @Override
    public boolean processRecord(MetaDataRecord<?> mdr, ExecutionContext context)
            throws CorruptedMetadataRecordException, IngestionPluginFailedException {
        counter++;
        if (counter % 50 == 0) {
            log.info("Dummy plugin is processing MDR " + mdr.getId());
        }
        return true;
    }

    @Override
    public String getName() {
        return DummyPlugin.class.getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Logging number of processed metadata and nothing more.";
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

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void initialize(ExecutionContext context) throws IngestionPluginFailedException {
        // nothing to do
    }

    @Override
    public void completed(ExecutionContext context) throws IngestionPluginFailedException {
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
