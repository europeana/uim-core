/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar;

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

    /** when last time data loaded to uim */
    COLLECTION_LAST_LOADED_DATE,

    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_TRANSFORM_RECORDS,

    /** date of acceptance indexing finished */
    COLLECTION_LAST_TRANSFORM_DATE,

    /** number of records enriched in execution */
    COLLECTION_LAST_ENRICH_RECORDS,

    /** date of enrichment finished */
    COLLECTION_LAST_ENRICH_DATE,

    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_CLUSTER_RECORDS,

    /** date of acceptance indexing finished */
    COLLECTION_LAST_CLUSTER_DATE,

    /** number of records indexed for acceptance in execution */
    COLLECTION_LAST_ACCEPTANCE_RECORDS,

    /** date of acceptance indexing finished */
    COLLECTION_LAST_ACCEPTANCE_DATE,

    /** number of records modified in indexed execution */
    COLLECTION_LAST_INDEXED_RECORDS,

    /** date of indexing finished */
    COLLECTION_LAST_INDEXED_DATE,

    /** number of records modified in lod execution */
    COLLECTION_LAST_LOD_RECORDS,

    /** date of lod finished */
    COLLECTION_LAST_LOD_DATE,

    /** number of newspaper records processed into uim */
    COLLECTION_LAST_NEWSPAPER_RECORDS,

    /** when last time newspaper records processed into uim */
    COLLECTION_LAST_NEWSPAPER_DATE,

    /** number of total records indexed for collection */
    COLLECTION_INDEXED_RECORDS,

    /** number of total records in lod */
    COLLECTION_LOD_RECORDS,

    /** number of total records loaded for collection */
    COLLECTION_LOADED_RECORDS,

    /** number of total fulltext objects loaded for collection */
    COLLECTION_FULLTEXT_OBJECTS,

    /** number of total digital objects loaded for collection */
    COLLECTION_DIGITAL_OBJECTS,

    /** number of total digital objects stored at TEL */
    COLLECTION_DIGITAL_STORED_OBJECTS,

    /** execution id of validation run */
    COLLECTION_LINK_VALIDATION,

    /** execution id of validation run */
    COLLECTION_FIELD_VALIDATION,

    /** execution id of validation run */
    COLLECTION_EDM_VALIDATION,

    /** redistribute metadata profile */
    COLLECTION_METADATA_PROFILE,

    /** redistribute metadata licence */
    COLLECTION_METADATA_LICENCE,

    /** collection status */
    COLLECTION_STATUS,

    /** provider consortia name for a provider */
    PROVIDER_CONSORTIA_NAME,

    /** SugarControlledVocabulary PARTNERSHIP_AGREEMENT_STATUS */
    PARTNERSHIP_AGREEMENT_STATUS,
    
    /** SugarControlledVocabulary RESTRICTION_BY_DISTRIB_FORMAT */
    RESTRICTION_BY_DISTRIB_FORMAT,
    
    /** SugarControlledVocabulary TEL_OTHER_DISTRIBUTION_FORMATS */
    TEL_OTHER_DISTRIBUTION_FORMATS,
    
    /** SugarControlledVocabulary TEL_SOURCE_DATA_LICENCE */
    TEL_SOURCE_DATA_LICENCE,
    
    /** SugarControlledVocabulary TEL_OTHER_SOURCE_DATA_LICENSE */
    TEL_OTHER_SOURCE_DATA_LICENSE,
    
    /** SugarControlledVocabulary TEL_FIELD_OF_TIME_RESTRICTION */
    TEL_FIELD_OF_TIME_RESTRICTION,
    
    /** SugarControlledVocabulary TEL_DURATION_OF_RESTRICTION */
    TEL_DURATION_OF_RESTRICTION,
    
    /** SugarControlledVocabulary TEL DISTRIBUTION FORMAT LICENCE */
    TEL_DISTRIBUTION_FORMAT_LICENCE;

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
