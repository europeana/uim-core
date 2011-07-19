/* IllegalIngestionPlugin.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * This is a minimal plugin containing non-static member fields. This should throw an exception.
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Apr 27, 2011
 */
@SuppressWarnings("unused")
public class IllegalIngestionPlugin implements IngestionPlugin {
    private static String constantConstant = "This is okay";

    private String        soNotRight;

    @Override
    public String getIdentifier() {
        return getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return "Evil Fields Plugins";
    }

    @Override
    public String getDescription() {
        return ("This plugin has evil non-static member fields ");
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
    public <I>void initialize(ExecutionContext<I> context) throws IngestionPluginFailedException {

    }

    @Override
    public <I>void completed(ExecutionContext<I> context) throws IngestionPluginFailedException {

    }

    @Override
    public <I>boolean processRecord(MetaDataRecord<I> mdr, ExecutionContext<I> context) {
        return true;

    }

}
