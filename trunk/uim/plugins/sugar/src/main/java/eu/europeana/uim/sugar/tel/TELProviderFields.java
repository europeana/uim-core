/* TELRetrievableField.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugarcrm.model.RetrievableField;
import eu.europeana.uim.sugarcrm.model.UpdatableField;

/**
 * TEL specific fields to be retrieved from the SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 15, 2011
 */
@SuppressWarnings("all")
public enum TELProviderFields implements RetrievableField, UpdatableField {
    ID("id", "accounts.id", null, "ID"),

    MNEMONIC("name_id_c", "accounts_cstm.name_id_c", StandardControlledVocabulary.MNEMONIC,
             "Mnemonic"),

    NAME("name", "accounts.name", StandardControlledVocabulary.NAME, "Name"),

    ALT_NAME("name_alt_c", "accounts_cstm.name_alt_c", null, "Alternate name"),

    ACRONYM("name_acronym_c", "accounts_cstm.name_acronym_c", null, "Acronym"),

    PHONE("phone_office", "accounts.phone_office", null, "Phone"),

    FACSIMILE("phone_fax", "accounts.phone_fax", null, "Facsimile"),

    LOGO("tel_logo_c", "accounts_cstm.tel_logo_c", null, "Logo"),

    WEBSITE("website", "accounts.website", null, "Website"),

    WIKIPEDIA("tel_wikipedia_link_native_c", "accounts_cstm.tel_wikipedia_link_native_c", null,
              "Wikipedia"),

    WIKIPEDIA_EN("tel_wikipedia_link_english_c", "accounts_cstm.tel_wikipedia_link_english_c",
                 null, "Wikipedia EN"),
    
    LINK_CONTACT("tel_partner_link_contact_c","accounts.tel_partner_link_contact_c",null,"Link to Contact information"),
    
    LINK_OPENING("accounts.tel_partner_link_opening_c","accounts.tel_partner_link_opening_c",null,"Link to Opening information"),
    IMAGE_LIST("tel_orgimages_c", "accounts_cstm.tel_orgimages_c", null, "Image List"),

    EMAIL("email1", "email1", null, "EMail"),

    COUNTRY("country_c", "accounts_cstm.country_c", StandardControlledVocabulary.COUNTRY, "Country"),

    LONGITUDE("tel_longitude_c", "accounts_cstm.tel_longitude_c", null, "Longitude"),

    LATIITUDE("tel_latitude_c", "accounts_cstm.tel_latitude_c", null, "Latitude"),

    ALTITUDE("tel_altitude_c", "accounts_cstm.tel_altitude_c", null, "Altitude"),

    TEL_PROVIDER_KIND("account_type", "accounts.account_type", StandardControlledVocabulary.TYPE,
                      "Provider kind"),

    TEL_ACTIVE_PORTAL("tel_inportal_c", "accounts_cstm.tel_inportal_c", null, "Active in portal"),

    TEL_ACTIVE_UIM("tel_inuim_c", "accounts_cstm.tel_inuim_c", StandardControlledVocabulary.ACTIVE,
                   "Active in UIM"),

    TEL_CAROUSEL_MANDATORY("tel_carousel_mandatory_c", "accounts_cstm.tel_carousel_mandatory_c",
                           null, "Carousel Mandatory"),

    TEL_CAROUSEL_CANDIDATE("tel_carousel_candidate_c", "accounts_cstm.tel_carousel_candidate_c",
                           null, "Carousel Candidate"),

    REPOX_SERVER("default_oai_url_c", "accounts_cstm.default_oai_url_c",
                 StandardControlledVocabulary.INTERNAL_OAI_BASE, "REPOX server"),

    REPOX_PREFIX("default_oai_metadataprefix_c", "accounts_cstm.default_oai_metadataprefix_c",
                 StandardControlledVocabulary.INTERNAL_OAI_BASE, "REPOX prefix"),

    DATE_MODIFIED("date_modified", "accounts.date_modified", null, "Date of Modification"), 
    
    ;

    private final String                       fieldId;
    private final String                       qualifiedFieldId;
    private final ControlledVocabularyKeyValue key;
    private final String                       description;

    private TELProviderFields(String fieldId, String qualifiedFieldId,
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
    public String getDescription() {
        return description;
    }

    @Override
    public ControlledVocabularyKeyValue getMappingField() {
        return key;
    }
}
