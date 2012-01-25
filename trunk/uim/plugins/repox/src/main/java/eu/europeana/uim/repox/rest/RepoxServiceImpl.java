/* RepoxServiceImpl.java - created on Jan 23, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.repox.DataSourceOperationException;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.rest.client.RepoxRestClient;
import eu.europeana.uim.repox.rest.client.RepoxRestClientFactory;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Source;
import eu.europeana.uim.repox.rest.utils.DatasourceType;
import eu.europeana.uim.repox.rest.utils.RepoxControlledVocabulary;
import eu.europeana.uim.repox.rest.utils.XmlObjectFactory;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;

/**
 * Default implementation of {@link RepoxService} based on REST Repox client.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 23, 2012
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RepoxServiceImpl implements RepoxService {
    /**
     * factory to retrieve (implicitly create) repox rest clients for specific repox locations
     */
    private RepoxRestClientFactory clientfactory;

    /**
     * registry to update changed data
     */
    private Registry               registry;

    /**
     * factory to create xml objects from UIM objects
     */
    private XmlObjectFactory       xmlFactory;

    /**
     * Creates a new instance of this class.
     */
    public RepoxServiceImpl() {
        this(null, null, null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param factory
     *            factory to retrieve (implicitly create) repox rest clients for specific repox
     *            locations
     * @param registry
     *            registry to update changed data
     * @param xmlFactory
     *            factory to create xml objects from UIM objects
     */
    public RepoxServiceImpl(RepoxRestClientFactory factory, Registry registry,
                            XmlObjectFactory xmlFactory) {
        this.clientfactory = factory;
        this.registry = registry;
        this.xmlFactory = xmlFactory;
    }

    /**
     * @param registry
     *            registry to update changed data
     */
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * @return registry to update changed data
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * @return factory to retrieve (implicitly create) repox rest clients for specific repox
     *         locations
     */
    public RepoxRestClientFactory getClientfactory() {
        return clientfactory;
    }

    /**
     * @param clientfactory
     *            factory to retrieve (implicitly create) repox rest clients for specific repox
     *            locations
     */
    public void setClientfactory(RepoxRestClientFactory clientfactory) {
        this.clientfactory = clientfactory;
    }

    /**
     * @return factory to create xml objects from UIM objects
     */
    public XmlObjectFactory getXmlFactory() {
        return xmlFactory;
    }

    /**
     * @param xmlFactory
     *            factory to create xml objects from UIM objects
     */
    public void setXmlFactory(XmlObjectFactory xmlFactory) {
        this.xmlFactory = xmlFactory;
    }

    @Override
    public void updateProvider(Provider<?> provider) throws RepoxException {
        if (provider.isAggregator()) { throw new RepoxException(
                "The requested object is not a Provider"); }

        RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());

        String aggregatorId = provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        if (aggregatorId == null) {
            String countryCode = provider.getValue(StandardControlledVocabulary.COUNTRY);
            if (countryCode != null) {
                Aggregator aggregator = xmlFactory.createAggregator(provider);

                Aggregator createdAggregator = client.createAggregator(aggregator);
                aggregatorId = createdAggregator.getId();
            } else {
                aggregatorId = "euaggregatorr0";
            }
            provider.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID, aggregatorId);
        }

        String providerId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        if (providerId != null) {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            jaxbProv.setId(providerId);
            client.updateProvider(jaxbProv);
        } else {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            Aggregator aggr = new Aggregator();
            aggr.setId(aggregatorId);

            eu.europeana.uim.repox.rest.client.xml.Provider createdProv = client.createProvider(
                    jaxbProv, aggr);

            provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, createdProv.getId());

            StorageEngine engine = registry.getStorageEngine();

            // Store the created RepoxID into the UIM object
            try {
                engine.updateProvider(provider);
            } catch (StorageEngineException e) {
                throw new RepoxException("Storing an ID to the UIM Provider object failed");
            }
        }
    }

    @Override
    public void deleteProvider(Provider<?> provider) throws RepoxException {
        String id = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        if (id == null) {
            RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
            client.deleteProvider(id);
        }
    }

    @Override
    public void updateCollection(Collection<?> collection) throws RepoxException {
        RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

        String htypeString = collection.getValue(RepoxControlledVocabulary.HARVESTING_TYPE);

        if (htypeString == null) { throw new DataSourceOperationException(
                "Error during the creation of a Datasource: "
                        + "HARVESTING_TYPE information not available in UIM for the specific object."); }

        DatasourceType harvestingtype = DatasourceType.valueOf(htypeString);

        String collectionId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (collectionId == null) {
            Source ds = xmlFactory.createDataSource(collection);

            eu.europeana.uim.repox.rest.client.xml.Provider jibxProv = new eu.europeana.uim.repox.rest.client.xml.Provider();
            jibxProv.setId(collection.getProvider().getValue(
                    RepoxControlledVocabulary.PROVIDER_REPOX_ID));

            Source retsource = null;
            switch (harvestingtype) {
            case oai_pmh:
                retsource = client.createDatasourceOAI(ds, jibxProv);
                break;
            case z39_50:
                retsource = client.createDatasourceZ3950Timestamp(ds, jibxProv);
                break;
            case ftp:
                retsource = client.createDatasourceFtp(ds, jibxProv);
                break;
            case http:
                retsource = client.createDatasourceHttp(ds, jibxProv);
                break;
            case folder:
                retsource = client.createDatasourceFolder(ds, jibxProv);
                break;
            }

            if (retsource == null) { throw new RepoxException(
                    "Could not create source for collection '" + collection.getMnemonic() + "'!"); }

            collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, retsource.getId());

            StorageEngine engine = registry.getStorageEngine();
            try {
                engine.updateCollection(collection);
                engine.checkpoint();
            } catch (StorageEngineException e) {
                throw new DataSourceOperationException(
                        "Storing the returned Repox id to the UIM collection object failed.");
            }

        } else {
            Source ds = xmlFactory.createDataSource(collection);
            ds.setId(collectionId);

            switch (harvestingtype) {
            case oai_pmh:
                client.updateDatasourceOAI(ds);
                break;
            case z39_50:
                client.updateDatasourceZ3950Timestamp(ds);
                break;
            case ftp:
                client.updateDatasourceFtp(ds);
                break;
            case http:
                client.updateDatasourceHttp(ds);
                break;
            case folder:
                client.updateDatasourceFolder(ds);
                break;
            }
        }
    }

    @Override
    public void deleteCollection(Collection<?> collection) throws RepoxException {
        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id == null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
            client.deleteProvider(id);
        }
    }
}
