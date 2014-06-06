/* TELCollectionTranslationFields.java - created on Sep 3, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.sugar.model.RetrievableField;
import eu.europeana.uim.sugar.model.UpdatableField;

/**
 * Defines fields from sugar for collections descriptions.
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Sep 3, 2012
 */
public enum TELCollectionTranslationFields implements RetrievableField, UpdatableField {
    /** 
     * language field from sugar
     */
    LANGUAGE("language_iso3", "telda_TEL_collection_descriptions.language_iso3", null,
             "ISO3 string of the language"),
    /** 
     * title field from sugar
     */
    TITLE("title", "telda_TEL_collection_descriptions.title", null,
          "Localized title of the collection"),
    /** 
     * description field from sugar
     */
    DESCRIPTION("description", "telda_TEL_collection_descriptions.title", null,
                "Localized description of the collection");

    private final String                       fieldId;
    private final String                       qualifiedFieldId;
    private final ControlledVocabularyKeyValue key;
    private final String                       description;

    private TELCollectionTranslationFields(String fieldId, String qualifiedFieldId,
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
