/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.utils;

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
    HARVESTING_TYPE;

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
