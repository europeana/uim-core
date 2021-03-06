/* SugarCRMServiceImpl.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.Language;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugar.client.SugarClient;
import eu.europeana.uim.sugar.client.SugarMapping;
import eu.europeana.uim.sugar.utils.SugarUtil;
import eu.europeana.uim.sugarcrm.SugarException;
import eu.europeana.uim.sugarcrm.SugarService;
import eu.europeana.uim.sugarcrm.model.RetrievableField;
import eu.europeana.uim.sugarcrm.model.UpdatableField;

/**
 * TEL specific implementation of the SugarCRM service
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 15, 2011
 */
public class SugarServiceImpl implements SugarService {

    private Date         sessionCreate = null;
    private String       sessionID     = null;

    private SugarClient  sugarClient;
    private SugarMapping sugarMapping;

    private String       sugarMappingClass;

    /**
     * Creates a new instance of this class.
     * 
     */
    public SugarServiceImpl() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param client
     */
    public SugarServiceImpl(SugarClient client) {
        this.setSugarClient(client);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param client
     * @param mapping
     */
    public SugarServiceImpl(SugarClient client, SugarMapping mapping) {
        this.setSugarClient(client);
        this.setSugarMapping(mapping);
    }

    @Override
    public boolean hasActiveSession() throws SugarException {
        return sessionID != null;
    }

    @Override
    public String login() throws SugarException {
        sessionID = null;
        try {
            sessionID = getSugarClient().login();
            sessionCreate = new Date();
            return sessionID;
        } catch (SugarException e) {
            throw new SugarException("Could not login to SugarCRM" + e);
        }
    }

    @Override
    public void logout() throws SugarException {
        if (sessionID != null) {
            getSugarClient().logout(sessionID);
        }
        sessionID = null;
    }

    @Override
    public void updateProvider(Provider<?> provider) throws SugarException {
        SugarClient client = validateConnection();

        String mnemonic = provider.getMnemonic();
        Map<String, String> sugarprovider = client.getProvider(sessionID, mnemonic);

        boolean update = false;

        Map<String, String> updates = new HashMap<String, String>();
        UpdatableField[] fields = getSugarMapping().getProviderUpdateableFields();
        for (UpdatableField field : fields) {
            String value = sugarprovider.get(field.getFieldId());

            if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) {
                // well this cannot change.

            } else if (StandardControlledVocabulary.NAME.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getName())) {
                    updates.put(field.getFieldId(), provider.getName());
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_BASE.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getOaiBaseUrl())) {
                    updates.put(field.getFieldId(), provider.getOaiBaseUrl());
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_PREFIX.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getOaiMetadataPrefix())) {
                    updates.put(field.getFieldId(), provider.getOaiMetadataPrefix());
                    update = true;
                }
            } else {
                // deal with values on the map.
                String pValue = provider.getValue(field.getMappingField());
                if (!StringUtils.equals(value, pValue)) {
                    updates.put(field.getFieldId(), pValue);
                    update = true;
                }
            }
        }

        if (update) {
            @SuppressWarnings("unused")
            boolean changed = client.updateProvider(sessionID, mnemonic, updates);
        }
    }

    @Override
    public boolean synchronizeProvider(Provider<?> provider) throws SugarException {
        if (provider == null || provider.getMnemonic() == null || provider.getMnemonic().isEmpty())
            throw new NullPointerException("Provider and provider mnemonic must not be null.");

        SugarClient client = validateConnection();
        Map<String, String> sugarprovider = client.getProvider(sessionID, provider.getMnemonic());

        return synchronizeProvider(provider, sugarprovider);
    }

    @Override
    public boolean synchronizeProvider(Provider<?> provider, Map<String, String> sugarprovider)
            throws SugarException {
        if (provider == null || provider.getMnemonic() == null || provider.getMnemonic().isEmpty())
            throw new NullPointerException("Provider and provider mnemonic must not be null.");

        boolean update = false;
        RetrievableField[] fields = getSugarMapping().getProviderRetrievableFields();
        for (RetrievableField field : fields) {
            String value = sugarprovider.get(field.getFieldId());

            if (StandardControlledVocabulary.ACTIVE.equals(field.getMappingField())) {
                if ("0".equals(value)) {
                    update = false;
                    break;
                }

            } else if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) {
                // well this cannot change.

            } else if (StandardControlledVocabulary.NAME.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getName())) {
                    provider.setName(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_BASE.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getOaiBaseUrl())) {
                    provider.setOaiBaseUrl(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_PREFIX.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, provider.getOaiMetadataPrefix())) {
                    provider.setOaiMetadataPrefix(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.COUNTRY.equals(field.getMappingField())) {
                Country country = SugarUtil.normalizeSugarCountry(value);

                value = country.getIso3();
                String pValue = provider.getValue(field.getMappingField());

                if (!StringUtils.equals(value, pValue)) {
                    provider.putValue(field.getMappingField(), value);
                    update = true;
                }
            } else {
                // deal with values on the map.
                // this includes country and type
                String pValue = provider.getValue(field.getMappingField());
                if (!StringUtils.equals(value, pValue)) {
                    provider.putValue(field.getMappingField(), value);
                    update = true;
                }
            }
        }
        return update;
    }

    @Override
    public void updateCollection(Collection<?> collection) throws SugarException {
        SugarClient client = validateConnection();

        String mnemonic = collection.getMnemonic();
        Map<String, String> sugarprovider = client.getCollection(sessionID, mnemonic);

        boolean update = false;

        Map<String, String> updates = new HashMap<String, String>();
        UpdatableField[] fields = getSugarMapping().getCollectionUpdateableFields();
        for (UpdatableField field : fields) {
            String value = sugarprovider.get(field.getFieldId());

            if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) {
                // well this cannot change.

            } else if (StandardControlledVocabulary.NAME.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getName())) {
                    updates.put(field.getFieldId(), collection.getName());
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_BASE.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiBaseUrl(false))) {
                    updates.put(field.getFieldId(), collection.getOaiBaseUrl(false));
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_SET.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiSet())) {
                    updates.put(field.getFieldId(), collection.getOaiSet());
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_PREFIX.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiMetadataPrefix(false))) {
                    updates.put(field.getFieldId(), collection.getOaiMetadataPrefix(false));
                    update = true;
                }
            } else {
                // deal with values on the map.
                String pValue = collection.getValue(field.getMappingField());
                if (!StringUtils.equals(value, pValue)) {
                    updates.put(field.getFieldId(), pValue);
                    update = true;
                }
            }
        }

        if (update) {
            @SuppressWarnings("unused")
            boolean changed = client.updateCollection(sessionID, mnemonic, updates);
        }

    }

    @Override
    public boolean synchronizeCollection(Collection<?> collection) throws SugarException {
        if (collection == null || collection.getMnemonic() == null ||
            collection.getMnemonic().isEmpty())
            throw new NullPointerException("Collection and collection mnemonic must not be null.");

        SugarClient client = validateConnection();
        Map<String, String> sugarprovider = client.getCollection(sessionID,
                collection.getMnemonic());

        return synchronizeCollection(collection, sugarprovider);
    }

    @Override
    public boolean synchronizeCollection(Collection<?> collection, Map<String, String> sugarprovider)
            throws SugarException {

        boolean update = false;
        RetrievableField[] fields = getSugarMapping().getCollectionRetrievableFields();
        for (RetrievableField field : fields) {
            String value = sugarprovider.get(field.getFieldId());

            if (StandardControlledVocabulary.ACTIVE.equals(field.getMappingField())) {
                if ("0".equals(value)) {
                    update = false;
                    break;
                }

            } else if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) {
                // well this cannot change.

            } else if (StandardControlledVocabulary.NAME.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getName())) {
                    collection.setName(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_BASE.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiBaseUrl(false))) {
                    collection.setOaiBaseUrl(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_SET.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiSet())) {
                    collection.setOaiSet(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.INTERNAL_OAI_PREFIX.equals(field.getMappingField())) {
                if (!StringUtils.equals(value, collection.getOaiMetadataPrefix(false))) {
                    collection.setOaiMetadataPrefix(value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.COUNTRY.equals(field.getMappingField())) {
                Country country = SugarUtil.normalizeSugarCountry(value);

                value = country.getIso3();
                String pValue = collection.getValue(field.getMappingField());

                if (!StringUtils.equals(value, pValue)) {
                    collection.putValue(field.getMappingField(), value);
                    update = true;
                }
            } else if (StandardControlledVocabulary.LANGUAGE.equals(field.getMappingField())) {
                Language language = SugarUtil.normalizeSugarLanguage(value);

                value = language.getIso3();
                String pValue = collection.getValue(field.getMappingField());

                if (!StringUtils.equals(value, pValue)) {
                    collection.putValue(field.getMappingField(), value);
                    update = true;
                }
            } else {
                // deal with values on the map.
                // this includes country and type
                String pValue = collection.getValue(field.getMappingField());
                if (!StringUtils.equals(value, pValue)) {
                    collection.putValue(field.getMappingField(), value);
                    update = true;
                }
            }
        }
        return update;

    }

    @Override
    public String getProviderMnemonic(Map<String, String> values) throws SugarException {
        RetrievableField[] fields = getSugarMapping().getProviderRetrievableFields();
        for (RetrievableField field : fields) {
            String value = values.get(field.getFieldId());

            if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) { return value; }
        }
        return null;
    }

    @Override
    public List<Map<String, String>> listProviders(boolean activeOnly) throws SugarException {
        SugarClient client = validateConnection();

        List<Map<String, String>> providers = client.getProviders(sessionID, "", Integer.MAX_VALUE);

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        RetrievableField[] fields = getSugarMapping().getProviderRetrievableFields();

        for (Map<String, String> record : providers) {
            if ("1".equals(record.get("deleted"))) {
                continue;
            }

            HashMap<String, String> resultRecord = new HashMap<String, String>();
            for (RetrievableField field : fields) {
                if (StandardControlledVocabulary.ACTIVE.equals(field.getMappingField())) {
                    if (activeOnly) {
                        if ("0".equals(record.get(field.getFieldId()))) {
                            resultRecord = null;
                            break;
                        }
                    }
                } else {
                    for (Entry<String, String> entry : record.entrySet()) {
                        if (field.getFieldId().equals(entry.getKey())) {
                            // retrievable.
                            resultRecord.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            if (resultRecord != null) {
                result.add(resultRecord);
            }
        }
        return result;
    }

    @Override
    public String getCollectionMnemonic(Map<String, String> values) throws SugarException {
        RetrievableField[] fields = getSugarMapping().getCollectionRetrievableFields();
        for (RetrievableField field : fields) {
            String value = values.get(field.getFieldId());

            if (StandardControlledVocabulary.MNEMONIC.equals(field.getMappingField())) { return value; }
        }
        return null;
    }

    @Override
    public String getProviderForCollection(String mnemonic) throws SugarException {
        SugarClient client = validateConnection();
        return client.getProviderForCollection(sessionID, mnemonic);
    }

    @Override
    public List<Map<String, String>> listCollections(boolean activeOnly) throws SugarException {
        SugarClient client = validateConnection();

        List<Map<String, String>> collections = client.getCollections(sessionID, "",
                Integer.MAX_VALUE);

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        RetrievableField[] fields = getSugarMapping().getCollectionRetrievableFields();

        for (Map<String, String> record : collections) {
            if ("1".equals(record.get("deleted"))) {
                continue;
            }

            HashMap<String, String> resultRecord = new HashMap<String, String>();
            for (RetrievableField field : fields) {
                if (StandardControlledVocabulary.ACTIVE.equals(field.getMappingField())) {
                    if (activeOnly) {
                        if ("0".equals(record.get(field.getFieldId()))) {
                            resultRecord = null;
                            break;
                        }
                    }
                } else {
                    for (Entry<String, String> entry : record.entrySet()) {
                        if (field.getFieldId().equals(entry.getKey())) {
                            // retrievable.
                            resultRecord.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            if (resultRecord != null) {
                result.add(resultRecord);
            }
        }

        return result;
    }

    private SugarClient validateConnection() throws SugarException {
        SugarClient client = getSugarClient();
        if (client == null) { throw new SugarException(
                "Could not find sugar client implementation!"); }

        if (sessionCreate == null ||
            sessionCreate.before(new Date(new Date().getTime() - (4 * 1000 * 60 * 60)))) {
            sessionID = client.login();
        }
        if (sessionID == null) { throw new SugarException("Could not find a valid session!"); }
        

        return client;
    }

    /**
     * lazy loading of sugar crm manager.
     * 
     * @return the sugarClien
     */
    public SugarClient getSugarClient() {
        return sugarClient;
    }

    /**
     * Sets the sugarClient to the given value.
     * 
     * @param sugarClient
     *            the sugarClient to set
     */
    public void setSugarClient(SugarClient sugarClient) {
        this.sugarClient = sugarClient;
    }

    /**
     * Returns the sugarMapping.
     * 
     * @return the sugarMapping
     */
    public SugarMapping getSugarMapping() {
        return sugarMapping;
    }

    /**
     * Sets the sugarMapping to the given value.
     * 
     * @param sugarMapping
     *            the sugarMapping to set
     */
    public void setSugarMapping(SugarMapping sugarMapping) {
        this.sugarMapping = sugarMapping;
    }

    /**
     * Returns the sugarMappingClass.
     * 
     * @return the sugarMappingClass
     */
    public String getSugarMappingClass() {
        return sugarMappingClass;
    }

    /**
     * Sets the sugarMappingClass to the given value.
     * 
     * @param sugarMappingClass
     *            the sugarMappingClass to set
     */
    public void setSugarMappingClass(String sugarMappingClass) {
        this.sugarMappingClass = sugarMappingClass;

        try {
            Class<?> mapping = Class.forName(sugarMappingClass, true,
                    SugarServiceImpl.class.getClassLoader());
            setSugarMapping((SugarMapping)mapping.newInstance());
        } catch (Throwable e) {
            throw new RuntimeException("FAiled to load mapping class: <" + sugarMappingClass + ">",
                    e);
        }

    }

}
