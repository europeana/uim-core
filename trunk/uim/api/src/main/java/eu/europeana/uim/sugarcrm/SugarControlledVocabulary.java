/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugarcrm;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;

/**
 * Provides Repox specific vocabulary.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 23, 2012
 */
public enum SugarControlledVocabulary implements ControlledVocabularyKeyValue {
    /**
     * number of indexed records
     */
    COLLECTION_INDEXED_RECORDS,
    /**
     * last loaded records number
     */
    COLLECTION_LAST_LOADED_RECORDS,
    /**
     * last loaded date
     */
    COLLECTION_LAST_LOADED_DATE,
    /**
     * last indexed date
     */
    COLLECTION_LAST_INDEXED_RECORDS,
    /**
     * last indexed records number
     */
    COLLECTION_LAST_INDEXED_DATE;

    /**
     * Creates a new instance of this class.
     */
    private SugarControlledVocabulary() {
        // nothing to do
    }

    @Override
    public String getFieldId() {
        return this.toString();
    }
}
