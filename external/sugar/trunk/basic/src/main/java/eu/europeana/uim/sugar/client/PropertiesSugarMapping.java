/* MinimalSugarMapping.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugar.model.RetrievableField;
import eu.europeana.uim.sugar.model.UpdatableField;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 5, 2012
 */
public class PropertiesSugarMapping implements SugarMapping {
    private RetrievableField[] providerRetrievalbe   = null;
    private UpdatableField[]   providerUpdatealbe    = null;

    private RetrievableField[] collectionRetrievalbe = null;
    private UpdatableField[]   collectionUpdatealbe  = null;

    /**
     * Creates a new instance of this class.
     */
    public PropertiesSugarMapping() {
        try {
            Properties properties = new Properties();
            properties.load(PropertiesSugarMapping.class.getResourceAsStream("/sugarcrm.properties"));
            initialize(properties);
        } catch (IOException e) {
            throw new RuntimeException("Caused by IOException", e);
        }
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param properties
     */
    public PropertiesSugarMapping(Properties properties) {
        initialize(properties);
    }

    private void initialize(Properties properties) {
        List<RetrievableField> pro = new ArrayList<RetrievableField>();
        List<UpdatableField> uPro = new ArrayList<UpdatableField>();
        List<RetrievableField> col = new ArrayList<RetrievableField>();
        List<UpdatableField> uCol = new ArrayList<UpdatableField>();

        for (String property : properties.stringPropertyNames()) {
            if ("sugar.provider.mnemonic".equals(property)) {
                // not updateable
                MappingFieldImpl fieldImpl = new MappingFieldImpl(properties.getProperty(property),
                        StandardControlledVocabulary.MNEMONIC);
                pro.add(fieldImpl);
            } else if ("sugar.collection.mnemonic".equals(property)) {
                // not updateable
                MappingFieldImpl fieldImpl = new MappingFieldImpl(properties.getProperty(property),
                        StandardControlledVocabulary.MNEMONIC);
                col.add(fieldImpl);

            } else if (property.startsWith("sugar.provider.")) {
                StandardControlledVocabulary mapping = null;
                if ("sugar.provider.name".equals(property)) {
                    mapping = StandardControlledVocabulary.NAME;
                } else if ("sugar.provider.type".equals(property)) {
                    mapping = StandardControlledVocabulary.TYPE;
                } else if ("sugar.provider.country".equals(property)) {
                    mapping = StandardControlledVocabulary.COUNTRY;
                } else if ("sugar.provider.oaibase".equals(property)) {
                    mapping = StandardControlledVocabulary.INTERNAL_OAI_BASE;
                } else if ("sugar.provider.oaiprefix".equals(property)) {
                    mapping = StandardControlledVocabulary.INTERNAL_OAI_PREFIX;
                } else if ("sugar.provider.inuim".equals(property)) {
                    mapping = StandardControlledVocabulary.ACTIVE;
                }

                if (mapping != null) {
                    MappingFieldImpl fieldImpl = new MappingFieldImpl(
                            properties.getProperty(property), mapping);
                    pro.add(fieldImpl);
                    uPro.add(fieldImpl);
                } else {
                    throw new IllegalArgumentException("No mapping field for :" + property);
                }
            } else if (property.startsWith("sugar.collection.")) {
                StandardControlledVocabulary mapping = null;
                if ("sugar.collection.name".equals(property)) {
                    mapping = StandardControlledVocabulary.NAME;
                } else if ("sugar.collection.type".equals(property)) {
                    mapping = StandardControlledVocabulary.TYPE;
                } else if ("sugar.collection.country".equals(property)) {
                    mapping = StandardControlledVocabulary.COUNTRY;
                } else if ("sugar.collection.language".equals(property)) {
                    mapping = StandardControlledVocabulary.LANGUAGE;
                } else if ("sugar.collection.oaibase".equals(property)) {
                    mapping = StandardControlledVocabulary.INTERNAL_OAI_BASE;
                } else if ("sugar.collection.oaiset".equals(property)) {
                    mapping = StandardControlledVocabulary.INTERNAL_OAI_SET;
                } else if ("sugar.collection.oaiprefix".equals(property)) {
                    mapping = StandardControlledVocabulary.INTERNAL_OAI_PREFIX;
                } else if ("sugar.collection.repoxtype".equals(property)) {
                    mapping = StandardControlledVocabulary.REPOX_TYPE;
                } else if ("sugar.collection.inuim".equals(property)) {
                     mapping = StandardControlledVocabulary.ACTIVE;
                } 
//                else if ("sugar.collection.profile".equals(property)) {
//                    mapping = SugarControlledVocabulary.COLLECTION_METADATA_PROFILE;
//                } else if ("sugar.collection.status".equals(property)) {
//                    mapping = SugarControlledVocabulary.COLLECTION_STATUS;
//                }

                if (mapping != null) {
                    MappingFieldImpl fieldImpl = new MappingFieldImpl(
                            properties.getProperty(property), mapping);
                    col.add(fieldImpl);
                    uCol.add(fieldImpl);
                } else {
                    throw new IllegalArgumentException("No mapping field for :" + property);
                }
            }
        }

        this.providerRetrievalbe = pro.toArray(new RetrievableField[0]);
        this.providerUpdatealbe = uPro.toArray(new UpdatableField[0]);
        this.collectionRetrievalbe = col.toArray(new RetrievableField[0]);
        this.collectionUpdatealbe = uCol.toArray(new UpdatableField[0]);
    }

    @Override
    public RetrievableField[] getProviderRetrievableFields() {
        return providerRetrievalbe;
    }

    @Override
    public UpdatableField[] getProviderUpdateableFields() {
        return providerUpdatealbe;
    }

    @Override
    public RetrievableField[] getCollectionRetrievableFields() {
        return collectionRetrievalbe;
    }

    @Override
    public UpdatableField[] getCollectionUpdateableFields() {
        return collectionUpdatealbe;
    }

    private static final class MappingFieldImpl implements RetrievableField, UpdatableField {

        private final String                       fieldId;
        private final String                       qualifiedId;
        private final ControlledVocabularyKeyValue key;

        public MappingFieldImpl(String qualifiedId, ControlledVocabularyKeyValue key) {
            super();
            if (qualifiedId.contains(".")) {
                this.fieldId = qualifiedId.substring(qualifiedId.lastIndexOf(".") + 1);
            } else {
                this.fieldId = qualifiedId;
            }
            this.qualifiedId = qualifiedId;
            this.key = key;
        }

        @Override
        public String getFieldId() {
            return fieldId;

        }

        @Override
        public String getQualifiedFieldId() {
            return qualifiedId;
        }

        @Override
        public ControlledVocabularyKeyValue getMappingField() {
            return key;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String toString() {
            return getFieldId() + "->" + getMappingField();
        }
    }

}
