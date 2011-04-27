/* IllegalIngestionPlugin.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.common.TKey;

/**
 * This is a minimal plugin.
 * This should not throw an exception.
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Apr 27, 2011
 */
@SuppressWarnings("unused")
public class LegalIngestionPlugin implements IngestionPlugin {

    
    private static String constantConstant = "This is okay";
    
    
    @Override
    public String getName() {
        return LegalIngestionPlugin.class.getSimpleName();
    }

    @Override
    public String getDescription() {
        return ("This plugin has only non-evil fields ");
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
    public void initialize(ExecutionContext context) throws IngestionPluginFailedException {

    }

    @Override
    public void completed(ExecutionContext context) throws IngestionPluginFailedException {
       
    }

    @Override
    public boolean processRecord(MetaDataRecord<?> mdr, ExecutionContext context) {
            return true;

    }

}
