/* TELCollectionDescriptionFields.java - created on Jul 29, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;

/**
 * 
 * 
 * @author Simon Tzanakis (Simon.Tzanakis@theeuropeanlibrary.org)
 * @since Jul 29, 2014
 */
public enum TELCollectionDescriptionsFields {
    /** TELCollectionDescriptionsFields TELDATASET */
    TELDATASET("telda_tel_collection_descriptions_telda_tel_dataset_name", "telda_TEL_collection_descriptions.telda_tel_collection_descriptions_telda_tel_dataset_name", null, "The reprsenting dataset"),
    /** TELCollectionDescriptionsFields TELISO6393LANGUAGES */
    TELISO6393LANGUAGES("telda_tel_collection_descriptions_telda_tel_iso639_3_languages_name", "telda_TEL_collection_descriptions.telda_tel_collection_descriptions_telda_tel_iso639_3_languages_name", null, "Language of collection decription"),
    /** TELCollectionDescriptionsFields TITLE */
    TITLE("title", "telda_TEL_collection_descriptions.title", null, "Title of the dataset"),
    /** TELCollectionDescriptionsFields ALTERNATIVETITLE */
    ALTERNATIVETITLE("alternative_title_c", "telda_TEL_collection_descriptions.alternative_title_c", null, "Alternative title of the dataset");
    
    private final String                       fieldId;
    private final String                       qualifiedFieldId;
    private final ControlledVocabularyKeyValue key;
    private final String                       description;
    
    
    /**
     * Creates a new instance of this class.
     * @param fieldId
     * @param qualifiedFieldId
     * @param key
     * @param description
     */
    private TELCollectionDescriptionsFields(String fieldId, String qualifiedFieldId, ControlledVocabularyKeyValue key, String description) {
        this.fieldId = fieldId;
        this.qualifiedFieldId = qualifiedFieldId;
        this.key = key;
        this.description = description;
    }


    /**
     * Returns the fieldId.
     * @return the fieldId
     */
    public String getFieldId() {
        return fieldId;
    }


    /**
     * Returns the qualifiedFieldId.
     * @return the qualifiedFieldId
     */
    public String getQualifiedFieldId() {
        return qualifiedFieldId;
    }


    /**
     * Returns the key.
     * @return the key
     */
    public ControlledVocabularyKeyValue getKey() {
        return key;
    }


    /**
     * Returns the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
