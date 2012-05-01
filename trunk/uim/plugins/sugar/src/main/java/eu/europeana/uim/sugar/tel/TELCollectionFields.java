/* TELRetrievableField.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugarcrm.SugarControlledVocabulary;
import eu.europeana.uim.sugarcrm.model.RetrievableField;
import eu.europeana.uim.sugarcrm.model.UpdatableField;

/**
 * TEL specific fields to be retrieved from the SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 15, 2011
 */
@SuppressWarnings("all")
public enum TELCollectionFields implements RetrievableField, UpdatableField {
    ID("id", "telda_tel_dataset.id", null, "ID"),

    MNEMONIC("tel_identifier_c", "telda_tel_dataset.tel_identifier_c",
             StandardControlledVocabulary.MNEMONIC, "Mnemonic"),

    NAME("name", "telda_tel_dataset.name", StandardControlledVocabulary.NAME, "Name"),

    LANGUAGE("telda_tel_dataset_telda_tel_iso639_3_languages_name",
             "telda_tel_dataset_telda_tel_iso639_3_languages_name",
             StandardControlledVocabulary.LANGUAGE, "Language of collection"),

    COUNTRY("country", "telda_tel_dataset.country", StandardControlledVocabulary.COUNTRY, "Country"),

    REPOX_SERVER("repox_server", "telda_tel_dataset.repox_server",
                 StandardControlledVocabulary.INTERNAL_OAI_BASE, "REPOX server"),

    REPOX_PREFIX("tel_repox_metadataprefix_c", "telda_tel_dataset.tel_repox_metadataprefix_c",
                 StandardControlledVocabulary.INTERNAL_OAI_PREFIX, "REPOX prefix"),

    TEL_COLLECTION_KIND("type_dataset", "telda_tel_dataset.type_dataset",
                        StandardControlledVocabulary.TYPE, "Dataset Type"),

    TEL_ACTIVE_UIM("tel_inuim_c", "telda_tel_dataset.tel_inuim_c",
                   StandardControlledVocabulary.ACTIVE, "Active in UIM"),

    TEL_ACTIVE_PORTAL("tel_inportal", "telda_tel_dataset.tel_inportal", null, "Active in portal"),

    TEL_PORTAL_STATUS("tel_portal_status", "telda_tel_dataset.tel_portal_status", null,
                      "Portal status"),

    TEL_PORTAL_CAROUSEL_MANDATORY("tel_carousel_mandatory",
                                  "telda_tel_dataset.tel_carousel_mandatory", null,
                                  "Carousel Mandatory"),

    TEL_PORTAL_CAROUSEL_CANDIDATE("tel_carousel_candidate",
                                  "telda_tel_dataset.tel_carousel_candidate", null,
                                  "Carousel Candidate"),

    TEL_PORTAL_FEDERATED_SRU_BASE("tel_srubase", "telda_tel_dataset.tel_srubase", null, "SRU Base"),

    TEL_PORTAL_FEDERATED_SRU_FORMAT("tel_sruformat", "telda_tel_dataset.tel_sruformat", null,
                                    "SRU Format"),

    TEL_PORTAL_EOD_SYSID("tel_eod_sysid", "telda_tel_dataset.tel_eod_sysid", null,
                         "EOD System Identifier"),

    TEL_PORTAL_EOD_IDMATCHER("tel_eod_idmatcher", "telda_tel_dataset.tel_eod_idmatcher", null,
                             "EOD IdMatcher"),

    TEL_PORTAL_EOD_CUTOFFYEAR("tel_eod_cutoffyear", "telda_tel_dataset.tel_eod_cutoffyear", null,
                              "EOD CuttOff Year"),

    TEL_PORTAL_EOD_BIBLIOGRAPHIC_LEVEL("tel_eod_bibliographiclevel",
                                       "telda_tel_dataset.tel_eod_bibliographiclevel", null,
                                       "EOD Bibl. Level"),

    TEL_DISCIPLINES("tel_disciplines", "telda_tel_dataset.tel_disciplines", null, "TEL Disciplines"),

    HARVESTING_METHOD("harvesting_method", "telda_tel_dataset.harvesting_method",
                      RepoxControlledVocabulary.HARVESTING_TYPE, "Harvesting method"),

    HARVESTING_STATUS("harvesting_status", "telda_tel_dataset.harvesting_status",
                      RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE, "Harvesting status"),

    HARVESTED_RECORDS("count_harvested_records", "telda_tel_dataset.count_harvested_records",
                      RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS,
                      "Number of harvested records"),

    // TODO: should come from repox but doesn't
    HARVESTING_DATE("tel_harvesting_date", "telda_tel_dataset.tel_harvesting_date", null,
                    "Harvesting Date"),

    // TODO: should come from repox but doesn't
    HARVESTING_UPDATE("tel_harvesting_update", "telda_tel_dataset.tel_harvesting_update", null,
                      "Harvesting Update"),

    LAST_LOADED_DATE("tel_last_loading_date", "telda_tel_dataset.tel_last_loading_date",
                     SugarControlledVocabulary.COLLECTION_LAST_LOADED_DATE, "Last UIM Loading Date"),

    LAST_LOADED_RECORDS("tel_last_loading_records", "telda_tel_dataset.tel_last_loading_records",
                        SugarControlledVocabulary.COLLECTION_LAST_LOADED_RECORDS,
                        "Number of loaded records"),

    LAST_TRANSFORM_DATE("tel_last_xslt_date", "telda_tel_dataset.tel_last_xslt_date",
                        SugarControlledVocabulary.COLLECTION_LAST_TRANSFORM_DATE,
                        "Last UIM XSLT Date"),

    LAST_TRANSFORM_RECORDS("tel_last_xslt_records", "telda_tel_dataset.tel_last_xslt_records",
                           SugarControlledVocabulary.COLLECTION_LAST_TRANSFORM_RECORDS,
                           "Number of transformed records"),

    LAST_CLUSTER_DATE("tel_last_cluster_date", "telda_tel_dataset.tel_last_cluster_date",
                      SugarControlledVocabulary.COLLECTION_LAST_CLUSTER_DATE,
                      "Last UIM Cluster Index Date"),

    LAST_CLUSTER_RECORDS("tel_last_cluster_records", "telda_tel_dataset.tel_last_cluster_records",
                         SugarControlledVocabulary.COLLECTION_LAST_CLUSTER_RECORDS,
                         "Number of cluster indexed records"),

    LAST_ENRICH_DATE("tel_last_enrich_date", "telda_tel_dataset.tel_last_enrich_date",
                     SugarControlledVocabulary.COLLECTION_LAST_ENRICH_DATE, "Last UIM Enrich Date"),

    LAST_ENRICH_RECORDS("tel_last_enrich_records", "telda_tel_dataset.tel_last_enrich_records",
                        SugarControlledVocabulary.COLLECTION_LAST_ENRICH_RECORDS,
                        "Number of enriched records"),

    LAST_ACCEPTANCE_DATE("tel_last_acceptance_date", "telda_tel_dataset.tel_last_acceptance_date",
                         SugarControlledVocabulary.COLLECTION_LAST_ACCEPTANCE_DATE,
                         "Last Acceptance Date"),

    LAST_ACCEPTANCE_RECORDS("tel_last_acceptance_records",
                            "telda_tel_dataset.tel_last_acceptance_records",
                            SugarControlledVocabulary.COLLECTION_LAST_ACCEPTANCE_RECORDS,
                            "Number of acceptance records"),

    INDEXED_RECORDS("count_indexed_records", "telda_tel_dataset.count_indexed_records",
                    SugarControlledVocabulary.COLLECTION_INDEXED_RECORDS,
                    "Total number of indexed records"),

    LAST_INDEXED_DATE("tel_last_indexed_date", "telda_tel_dataset.tel_last_indexed_date",
                      SugarControlledVocabulary.COLLECTION_LAST_INDEXED_DATE, "Last Indexing Date"),

    LAST_INDEXED_RECORDS("tel_last_indexed_records", "telda_tel_dataset.tel_last_indexed_records",
                         SugarControlledVocabulary.COLLECTION_LAST_INDEXED_RECORDS,
                         "Number of indexed records"),

    LOD_RECORDS("count_lod_records", "telda_tel_dataset.count_lod_records",
                SugarControlledVocabulary.COLLECTION_LOD_RECORDS, "Total number of lod records"),

    LAST_LOD_DATE("tel_last_lod_date", "telda_tel_dataset.tel_last_lod_date",
                  SugarControlledVocabulary.COLLECTION_LAST_LOD_DATE, "Last LOD Date"),

    LAST_LOD_RECORDS("tel_last_lod_records", "telda_tel_dataset.tel_last_lod_records",
                     SugarControlledVocabulary.COLLECTION_LAST_LOD_RECORDS, "Number of lod records"),

    LINKCHECK_EXECUTION("tel_linkcheck_execution", "telda_tel_dataset.tel_linkcheck_execution",
                        SugarControlledVocabulary.COLLECTION_LINK_VALIDATION, "Linkcheck execution"),

    FIELDCHECK_EXECUTION("tel_fieldcheck_execution", "telda_tel_dataset.tel_fieldcheck_execution",
                         SugarControlledVocabulary.COLLECTION_FIELD_VALIDATION,
                         "Fieldcheck execution"),

    DATE_MODIFIED("date_modified", "telda_tel_dataset.date_modified", null, "Date of Modification");

    private final String                       fieldId;
    private final String                       qualifiedFieldId;
    private final ControlledVocabularyKeyValue key;
    private final String                       description;

    private TELCollectionFields(String fieldId, String qualifiedFieldId,
                                ControlledVocabularyKeyValue key, String description) {
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
