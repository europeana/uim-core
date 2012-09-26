/* IllegalIngestionPlugin.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * This is a minimal plugin. This should not throw an exception.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Apr 27, 2011
 */
@SuppressWarnings("unused")
public class LegalIngestionPlugin<I> extends AbstractIngestionPlugin<MetaDataRecord<I>, I> {
    private static String constantConstant = "This is okay";

    /**
     * Creates a new instance of this class.
     */
    public LegalIngestionPlugin() {
        super("Non-evil Fields Plugins", "This plugin has only non-evil fields");
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
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<String> getParameters() {
        return new ArrayList<String>();
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
    public void initialize(ExecutionContext<MetaDataRecord<I>, I> context) throws IngestionPluginFailedException {

    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context) throws IngestionPluginFailedException {

    }

    @Override
    public boolean process(MetaDataRecord<I> mdr, ExecutionContext<MetaDataRecord<I>, I> context) {
        return true;
    }
}
