package eu.europeana.uim.api;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;

public interface IngestionPlugin {


    public TKey<MDRFieldRegistry, ?>[] getInputParameters();
    public TKey<MDRFieldRegistry, ?>[] getOutputParameters();
    public TKey<MDRFieldRegistry, ?>[] getTransientParameters();


    String getIdentifier();
    public String getDescription();

    int getPreferredThreadCount();
    int getMaximumThreadCount();

    /**
     * Process a single meta data record within a given execution context
     *
     * @param mdr     the {@link MetaDataRecord} to process
     * @param context the {@link ExecutionContext} for this processing call. This context can change for each call, so references to it have to be handled carefully.
     */
    public void processRecord(MetaDataRecord mdr, ExecutionContext context);


}
