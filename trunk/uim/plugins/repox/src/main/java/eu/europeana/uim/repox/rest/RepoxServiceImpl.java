/* RepoxServiceImpl.java - created on Jan 23, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import java.util.List;

import eu.europeana.uim.repox.DataSourceOperationException;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.rest.client.RepoxRestClient;
import eu.europeana.uim.repox.rest.client.RepoxRestClientFactory;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.HarvestingStatus;
import eu.europeana.uim.repox.rest.client.xml.Log;
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
public class RepoxServiceImpl implements RepoxService {
    /**
     * factory to retrieve (implicitly create) repox rest clients for specific repox locations
     */
    private RepoxRestClientFactory clientfactory;

    /**
     * factory to create xml objects from UIM objects
     */
    private XmlObjectFactory       xmlFactory;

    /**
     * Creates a new instance of this class.
     */
    public RepoxServiceImpl() {
        this(null, null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param factory
     *            factory to retrieve (implicitly create) repox rest clients for specific repox
     *            locations
     * @param xmlFactory
     *            factory to create xml objects from UIM objects
     */
    public RepoxServiceImpl(RepoxRestClientFactory factory, XmlObjectFactory xmlFactory) {
        this.clientfactory = factory;
        this.xmlFactory = xmlFactory;
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
        }
    }

    @Override
    public void deleteProvider(Provider<?> provider) throws RepoxException {
        String id = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
            client.deleteProvider(id);
            provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, null);
        }
    }

    @Override
    public void synchronizeProvider(Provider<?> provider) throws RepoxException {
// String id = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
// if (id == null) {
// RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
// eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = client.retrieveProvider(id);
// String xmlProvider = RepoxXmlUtils.marshall(jaxbProv);
// provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_XML, xmlProvider);
// }
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
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
            client.deleteDatasource(id);
            collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, null);
        }
    }

    @Override
    public void synchronizeCollection(Collection<?> collection) throws RepoxException {
        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

// eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = client.retrieveCollection(id);
// String xmlProvider = RepoxXmlUtils.marshall(jaxbProv);
// collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_XML, xmlProvider);

            HarvestingStatus status = client.getHarvestingStatus(id);
            collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE,
                    status.getStatus());
            collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS,
                    status.getRecords() != null ? status.getRecords() : "");
        }
    }

    @Override
    public String getHarvestLog(Collection<?> collection) throws RepoxException {
        StringBuffer sb = new StringBuffer();

        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
            Log harvestLog = client.getHarvestLog(id);

            List<String> linelist = harvestLog.getLine();
            for (String ln : linelist) {
                sb.append(ln);
            }
        }

        return sb.toString();
    }
}
