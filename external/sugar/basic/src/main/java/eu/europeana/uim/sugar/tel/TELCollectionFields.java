/* TELRetrievableField.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugar.SugarControlledVocabulary;
import eu.europeana.uim.sugar.model.RetrievableField;
import eu.europeana.uim.sugar.model.UpdatableField;

/**
 * TEL specific fields to be retrieved from the SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 15, 2011
 */
@SuppressWarnings("all")
public enum TELCollectionFields implements RetrievableField, UpdatableField {
    ID("id", "telda_tel_dataset.id", null, "ID"),

    MNEMONIC("tel_identifier_c", "telda_tel_dataset.tel_identifier_c", StandardControlledVocabulary.MNEMONIC, "Mnemonic"),

    NAME("name", "telda_tel_dataset.name", StandardControlledVocabulary.NAME, "Name"),

    LANGUAGE("telda_tel_dataset_telda_tel_iso639_3_languages_name", "telda_tel_dataset_telda_tel_iso639_3_languages_name", StandardControlledVocabulary.LANGUAGE, "Language of collection"),

    COUNTRY("country", "telda_tel_dataset.country", StandardControlledVocabulary.COUNTRY, "Country"),

    REPOX_SERVER("repox_server", "telda_tel_dataset.repox_server", StandardControlledVocabulary.INTERNAL_OAI_BASE, "REPOX server"),

    REPOX_PREFIX("tel_repox_metadataprefix_c", "telda_tel_dataset.tel_repox_metadataprefix_c", StandardControlledVocabulary.INTERNAL_OAI_PREFIX, "REPOX prefix"),

    TEL_COLLECTION_KIND("type_dataset", "telda_tel_dataset.type_dataset", StandardControlledVocabulary.TYPE, "Dataset Type"),

    TEL_ACTIVE_UIM("tel_inuim_c", "telda_tel_dataset.tel_inuim_c", StandardControlledVocabulary.ACTIVE, "Active in UIM"),

    TEL_ACTIVE_PORTAL("tel_inportal", "telda_tel_dataset.tel_inportal", null, "Active in portal"),

    TEL_IS_EUROPEANA("enriched_for_europeana_ese", "telda_tel_dataset.enriched_for_europeana_ese", null, "To be delivered to  Europeana"),

    TEL_PORTAL_STATUS("tel_portal_status", "telda_tel_dataset.tel_portal_status", null, "Portal status"),

    TEL_PORTAL_CAROUSEL_MANDATORY("tel_carousel_mandatory", "telda_tel_dataset.tel_carousel_mandatory", null, "Carousel Mandatory"),

    TEL_PORTAL_CAROUSEL_CANDIDATE("tel_carousel_candidate", "telda_tel_dataset.tel_carousel_candidate", null, "Carousel Candidate"),

    TEL_PORTAL_FEDERATED_SRU_BASE("tel_srubase", "telda_tel_dataset.tel_srubase", null, "SRU Base"),

    TEL_PORTAL_FEDERATED_SRU_FORMAT("tel_sruformat", "telda_tel_dataset.tel_sruformat", null, "SRU Format"),

    TEL_PORTAL_EOD_SYSID("tel_eod_sysid", "telda_tel_dataset.tel_eod_sysid", null, "EOD System Identifier"),

    TEL_PORTAL_EOD_IDMATCHER("tel_eod_idmatcher", "telda_tel_dataset.tel_eod_idmatcher", null, "EOD IdMatcher"),

    TEL_PORTAL_EOD_CUTOFFYEAR("tel_eod_cutoffyear", "telda_tel_dataset.tel_eod_cutoffyear", null, "EOD CuttOff Year"),

    TEL_PORTAL_EOD_BIBLIOGRAPHIC_LEVEL("tel_eod_bibliographiclevel", "telda_tel_dataset.tel_eod_bibliographiclevel", null, "EOD Bibl. Level"),

    TEL_DISCIPLINES("tel_disciplines", "telda_tel_dataset.tel_disciplines", null, "TEL Disciplines"),

    HARVESTING_METHOD("harvesting_method", "telda_tel_dataset.harvesting_method", RepoxControlledVocabulary.HARVESTING_TYPE, "Harvesting method"),

    HARVESTING_STATUS("harvesting_status", "telda_tel_dataset.harvesting_status", RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE, "Harvesting status"),

    HARVESTED_RECORDS("count_harvested_records", "telda_tel_dataset.count_harvested_records", RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS, "Number of harvested records"),

    HARVESTING_DATE("tel_harvesting_date", "telda_tel_dataset.tel_harvesting_date", RepoxControlledVocabulary.COLLECTION_HARVESTING_LAST_DATE, "Harvesting Date"),

    // TODO: should come from repox but doesn't
    HARVESTING_UPDATE("tel_harvesting_update", "telda_tel_dataset.tel_harvesting_update", RepoxControlledVocabulary.COLLECTION_HARVESTING_LAST_DATE, "Harvesting Update"),

    LAST_LOADED_DATE("tel_last_loading_date", "telda_tel_dataset.tel_last_loading_date", SugarControlledVocabulary.COLLECTION_LAST_LOADED_DATE, "Last UIM Loading Date"),

    LAST_LOADED_RECORDS("tel_last_loading_records", "telda_tel_dataset.tel_last_loading_records", SugarControlledVocabulary.COLLECTION_LAST_LOADED_RECORDS, "Number of loaded records"),

    LAST_TRANSFORM_DATE("tel_last_xslt_date", "telda_tel_dataset.tel_last_xslt_date", SugarControlledVocabulary.COLLECTION_LAST_TRANSFORM_DATE, "Last UIM XSLT Date"),

    LAST_TRANSFORM_RECORDS("tel_last_xslt_records", "telda_tel_dataset.tel_last_xslt_records", SugarControlledVocabulary.COLLECTION_LAST_TRANSFORM_RECORDS, "Number of transformed records"),

    LAST_CLUSTER_DATE("tel_last_cluster_date", "telda_tel_dataset.tel_last_cluster_date", SugarControlledVocabulary.COLLECTION_LAST_CLUSTER_DATE, "Last UIM Cluster Index Date"),

    LAST_CLUSTER_RECORDS("tel_last_cluster_records", "telda_tel_dataset.tel_last_cluster_records", SugarControlledVocabulary.COLLECTION_LAST_CLUSTER_RECORDS, "Number of cluster indexed records"),

    LAST_ENRICH_DATE("tel_last_enrich_date", "telda_tel_dataset.tel_last_enrich_date", SugarControlledVocabulary.COLLECTION_LAST_ENRICH_DATE, "Last UIM Enrich Date"),

    LAST_ENRICH_RECORDS("tel_last_enrich_records", "telda_tel_dataset.tel_last_enrich_records", SugarControlledVocabulary.COLLECTION_LAST_ENRICH_RECORDS, "Number of enriched records"),

    LAST_ACCEPTANCE_DATE("tel_last_acceptance_date", "telda_tel_dataset.tel_last_acceptance_date", SugarControlledVocabulary.COLLECTION_LAST_ACCEPTANCE_DATE, "Last Acceptance Date"),

    LAST_ACCEPTANCE_RECORDS("tel_last_acceptance_records", "telda_tel_dataset.tel_last_acceptance_records", SugarControlledVocabulary.COLLECTION_LAST_ACCEPTANCE_RECORDS, "Number of acceptance records"),

    LAST_INDEXED_DATE("tel_last_indexed_date", "telda_tel_dataset.tel_last_indexed_date", SugarControlledVocabulary.COLLECTION_LAST_INDEXED_DATE, "Last Indexing Date"),

    LAST_INDEXED_RECORDS("tel_last_indexed_records", "telda_tel_dataset.tel_last_indexed_records", SugarControlledVocabulary.COLLECTION_LAST_INDEXED_RECORDS, "Number of indexed records"),

    LAST_LOD_DATE("tel_last_lod_date", "telda_tel_dataset.tel_last_lod_date", SugarControlledVocabulary.COLLECTION_LAST_LOD_DATE, "Last LOD Date"),

    LAST_LOD_RECORDS("tel_last_lod_records", "telda_tel_dataset.tel_last_lod_records", SugarControlledVocabulary.COLLECTION_LAST_LOD_RECORDS, "Number of lod records"),

    LAST_NEWSPAPER_DATE("tel_last_newspaper_date_c", "telda_tel_dataset_cstm.tel_last_newspaper_date_c", SugarControlledVocabulary.COLLECTION_LAST_NEWSPAPER_DATE, "Last Newspaper Date"),

    LAST_NEWSPAPER_RECORDS("tel_last_newspaper_records_c", "telda_tel_dataset_cstm.tel_last_newspaper_records_c", SugarControlledVocabulary.COLLECTION_LAST_NEWSPAPER_RECORDS, "Number of newspaper records"),

    LOAD_RECORDS("count_loaded_records_c", "telda_tel_dataset_cstm.count_loaded_records_c", SugarControlledVocabulary.COLLECTION_LOADED_RECORDS, "Total number of loaded records"),

    INDEXED_RECORDS("count_indexed_records", "telda_tel_dataset.count_indexed_records", SugarControlledVocabulary.COLLECTION_INDEXED_RECORDS, "Total number of indexed records"),

    FULLTEXT_OBJECTS("count_fulltext_objects_c", "telda_tel_dataset_cstm.count_fulltext_objects_c", SugarControlledVocabulary.COLLECTION_FULLTEXT_OBJECTS, "Total number of fulltext objects"),

    DIGITAL_OBJECTS("count_digital_objects_c", "telda_tel_dataset_cstm.count_digital_objects_c", SugarControlledVocabulary.COLLECTION_DIGITAL_OBJECTS, "Total number of digital objects"),

    DIGITAL_STORED_OBJECTS("count_digital_stored_objects_c", "telda_tel_dataset_cstm.count_digital_stored_objects_c", SugarControlledVocabulary.COLLECTION_DIGITAL_STORED_OBJECTS, "Total number of digital objects stored"),

    LOD_RECORDS("count_lod_records", "telda_tel_dataset.count_lod_records", SugarControlledVocabulary.COLLECTION_LOD_RECORDS, "Total number of lod records"),

    LINKCHECK_EXECUTION("tel_linkcheck_execution", "telda_tel_dataset.tel_linkcheck_execution", SugarControlledVocabulary.COLLECTION_LINK_VALIDATION, "Linkcheck execution"),

    FIELDCHECK_EXECUTION("tel_fieldcheck_execution", "telda_tel_dataset.tel_fieldcheck_execution", SugarControlledVocabulary.COLLECTION_FIELD_VALIDATION, "Fieldcheck execution"),

    EDMCHECK_EXECUTION("tel_edmcheck_execution", "telda_tel_dataset.tel_edmcheck_execution", SugarControlledVocabulary.COLLECTION_EDM_VALIDATION, "Edmcheck execution"),

    METADATA_PROFILE("tel_redistribute_profiles_c", "telda_tel_dataset_cstm.tel_redistribute_profiles_c", SugarControlledVocabulary.COLLECTION_METADATA_PROFILE, "Redistribute Metadata Profile"),

    METADATA_LICENCE("tel_redistribute_rights", "telda_tel_dataset.tel_redistribute_rights", SugarControlledVocabulary.COLLECTION_METADATA_LICENCE, "Redistribute Metadata Licence"),

    STATUS("tel_dataset_status", "telda_tel_dataset.tel_dataset_status", SugarControlledVocabulary.COLLECTION_STATUS, "Collection status"),

    DATE_MODIFIED("date_modified", "telda_tel_dataset.date_modified", null, "Date of Modification"),

    DESCRIPTION_LINK_ENGLISH("tel_description_link_english", "telda_tel_dataset.tel_description_link_english", null, "Link to the collection description in English"),

    DESCRIPTION_LINK_NATIVE("tel_description_link_native", "telda_tel_dataset.tel_description_link_native", null, "Link to the collection description in the native language"),

    PARTNERSHIP_AGREEMENT_STATUS("partnership_agreement_status_c", "telda_tel_dataset.partnership_agreement_status_c", null, "The status of the patnership agreement"),

    RESTRICTION_BY_DISTRIB_FORMAT("restriction_by_distrib_format_c", "telda_tel_dataset.restriction_by_distrib_format_c", null, "Distribution format restriction"),

    TEL_OTHER_DISTRIBUTION_FORMATS("tel_other_distribution_formats_c", "telda_tel_dataset.tel_other_distribution_formats_c", null, "Other distribution formats"),

    TEL_SOURCE_DATA_LICENCE("tel_source_data_licence_c", "telda_tel_dataset.tel_source_data_licence_c", null, "Source data licence"),

    TEL_OTHER_SOURCE_DATA_LICENSE("tel_other_source_data_license_c", "telda_tel_dataset.tel_other_source_data_license_c", null, "Other Source data licence"),

    TEL_FIELD_OF_TIME_RESTRICTION("tel_field_of_time_restriction_c", "telda_tel_dataset.tel_field_of_time_restriction_c", null, "Restriction by release date - Field of time restriction"),

    TEL_DURATION_OF_RESTRICTION_C("tel_duration_of_restriction_c", "telda_tel_dataset.tel_duration_of_restriction_c", null, "TEL Restriction by release date - Duration of restriction");

    private final String                       fieldId;
    private final String                       qualifiedFieldId;
    private final ControlledVocabularyKeyValue key;
    private final String                       description;

    private TELCollectionFields(String fieldId, String qualifiedFieldId, ControlledVocabularyKeyValue key, String description) {
        this.fieldId = fieldId;
        this.qualifiedFieldId = qualifiedFieldId;
        this.key = key;
        this.description = description;
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }

    @Override
    public String getQualifiedFieldId() {
        return qualifiedFieldId;
    }

    @Override
    public ControlledVocabularyKeyValue getMappingField() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
