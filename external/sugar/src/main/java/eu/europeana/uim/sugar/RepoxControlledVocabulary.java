/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;

/**
 * Provides Repox specific vocabulary.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 23, 2012
 */
public enum RepoxControlledVocabulary implements ControlledVocabularyKeyValue {
    /**
     * repox identifier key used on maps of providers as ID of the repox aggregators (specific to
     * this implementation)
     */
    AGGREGATOR_REPOX_ID,
    /**
     * repox identifier key used on maps of providers as ID of the repox provider (specific to this
     * implementation)
     */
    PROVIDER_REPOX_ID,
    /**
     * repox identifier key used on maps of collections as ID of the repox collection (specific to
     * this implementation)
     */
    COLLECTION_REPOX_ID,
    /**
     * type of harvesting
     */
    HARVESTING_TYPE,
    /**
     * last time repox information on UIM object has been updated
     */
    LAST_UPDATE_DATE,
    /**
     * xml representation of provider
     */
    PROVIDER_REPOX_XML,
    /**
     * xml representation of collection
     */
    COLLECTION_REPOX_XML,
    /**
     * state of harvesting
     */
    COLLECTION_HARVESTING_STATE,
    /**
     * state of last harvesting
     */
    COLLECTION_HARVESTING_LAST_DATE,
    /**
     * number of harvested records
     */
    COLLECTION_HARVESTED_RECORDS;

    /**
     * Creates a new instance of this class.
     */
    private RepoxControlledVocabulary() {
        // nothing to do
    }

    @Override
    public String getFieldId() {
        return this.toString();
    }
}
