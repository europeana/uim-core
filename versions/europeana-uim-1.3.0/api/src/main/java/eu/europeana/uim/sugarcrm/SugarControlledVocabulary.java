/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugarcrm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;

/**
 * Provides Repox specific vocabulary.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 23, 2012
 */
public enum SugarControlledVocabulary implements ControlledVocabularyKeyValue {

    /** nubmer of records loaded into uim */
    COLLECTION_LAST_LOADED_RECORDS,
    
    /** when last time data loaded to uim*/
    COLLECTION_LAST_LOADED_DATE,

    
    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_TRANSFORM_RECORDS,
    
    /** date of acceptance indexing finished */
    COLLECTION_LAST_TRANSFORM_DATE,

    
    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_CLUSTER_RECORDS,
    
    /** date of acceptance indexing finished */
    COLLECTION_LAST_CLUSTER_DATE,

    
    /** number of records enriched in execution */
    COLLECTION_LAST_ENRICH_RECORDS,
    
    /** date of enrichment finished */
    COLLECTION_LAST_ENRICH_DATE,

    
    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_ACCEPTANCE_RECORDS,
    
    /** date of acceptance indexing finished */
    COLLECTION_LAST_ACCEPTANCE_DATE,
    
    
    /** number of total records indexed for collection */
    COLLECTION_INDEXED_RECORDS,

    /** number of records modified in indexed execution */
    COLLECTION_LAST_INDEXED_RECORDS,
    
    /** date of indexing finished */
    COLLECTION_LAST_INDEXED_DATE,
    
    
    /** number of total records in lod */
    COLLECTION_LOD_RECORDS,

    /** number of records modified in lod execution */
    COLLECTION_LAST_LOD_RECORDS,
    
    /** date of lod finished */
    COLLECTION_LAST_LOD_DATE,
    
    
    /** execution id of validation run */
    COLLECTION_LINK_VALIDATION,
    
    /** execution id of validation run */
    COLLECTION_FIELD_VALIDATION;

    
    /** SugarCRM date format */
    public static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    
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
