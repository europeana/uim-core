/* RepoxServiceImpl.java - created on Jan 23, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.rest.client.RepoxRestClient;
import eu.europeana.uim.repox.rest.client.RepoxRestClientFactory;
import eu.europeana.uim.repox.rest.client.RepoxRestClientFactoryImpl;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Aggregators;
import eu.europeana.uim.repox.rest.client.xml.HarvestingStatus;
import eu.europeana.uim.repox.rest.client.xml.Log;
import eu.europeana.uim.repox.rest.client.xml.RunningTasks;
import eu.europeana.uim.repox.rest.client.xml.Source;
import eu.europeana.uim.repox.rest.utils.BasicXmlObjectFactory;
import eu.europeana.uim.repox.rest.utils.DatasourceType;
import eu.europeana.uim.repox.rest.utils.RepoxXmlUtils;
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
    private static final Logger logger = Logger.getLogger(RepoxServiceImpl.class.getName());
    
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
        this(new RepoxRestClientFactoryImpl(), new BasicXmlObjectFactory());
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
        if (provider.getOaiBaseUrl() == null || provider.getOaiBaseUrl().length() == 0) { return; }

        RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
        updateProvider(client, provider, null);
    }

    private void updateProvider(RepoxRestClient client, Provider<?> provider,
            Collection<?> collection) throws RepoxException {
        String aggregatorId = updateAggregator(client, provider, collection);

        String providerId;
        if (collection != null) {
            providerId = collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        } else {
            providerId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        }

        eu.europeana.uim.repox.rest.client.xml.Provider retDs = client.retrieveProviderByNameCode(provider.getMnemonic());
        if (providerId == null && retDs != null) {
            providerId = retDs.getId();

            if (collection != null) {
                collection.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, retDs.getId());
            } else {
                provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, retDs.getId());
            }
        }

        if (providerId == null || retDs == null) {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            jaxbProv.setId(providerId);

            Aggregator aggr = new Aggregator();
            aggr.setId(aggregatorId);

            eu.europeana.uim.repox.rest.client.xml.Provider createdProv = client.createProvider(
                    jaxbProv, aggr);
            providerId = createdProv.getId();

            if (collection != null) {
                collection.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, providerId);
            } else {
                provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, providerId);
            }
        } else {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            jaxbProv.setId(providerId);
            client.updateProvider(jaxbProv);

        }
    }

    private String updateAggregator(RepoxRestClient client, Provider<?> provider,
            Collection<?> collection) throws RepoxException {
        String aggregatorId;
        if (collection != null) {
            aggregatorId = collection.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        } else {
            aggregatorId = provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        }

        if (provider.getValue(StandardControlledVocabulary.COUNTRY) == null) {
            provider.putValue(StandardControlledVocabulary.COUNTRY, "XXX");
        }
        Aggregator aggregator = xmlFactory.createAggregator(provider);

        Aggregator retDs = client.retrieveAggregatorByNameCode(aggregator.getNameCode());
        if (aggregatorId == null && retDs != null) {
            aggregatorId = retDs.getId();

            if (collection != null) {
                collection.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID, retDs.getId());
            } else {
                provider.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID, retDs.getId());
            }
        }
        

        if (aggregatorId == null || retDs == null) {
            aggregator.setId(aggregatorId);
            
            Aggregator createdAggregator = client.createAggregator(aggregator);
            aggregatorId = createdAggregator.getId();

            if (collection != null) {
                collection.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID, aggregatorId);
            } else {
                provider.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID, aggregatorId);
            }
        } else {
            // no updates to aggregators!
        }
        return aggregatorId;
    }

    @Override
    public void deleteProvider(Provider<?> provider) throws RepoxException {
        if (provider.getOaiBaseUrl() == null || provider.getOaiBaseUrl().length() == 0) { return; }

        String id = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
            client.deleteProvider(id);
            
            provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, null);
        }
    }

    @Override
    public void synchronizeProvider(Provider<?> provider) throws RepoxException {
        if (provider.getOaiBaseUrl() == null || provider.getOaiBaseUrl().length() == 0) { return; }

        String id = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = client.retrieveProvider(id);
            String xmlProvider;
            try {
                xmlProvider = RepoxXmlUtils.marshall(jaxbProv);
            } catch (JAXBException e) {
                throw new RuntimeException("Could not marshall source for id '" + id + "'!", e);
            }
            String storedXml = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_XML);
            if (storedXml == null || !storedXml.equals(xmlProvider)) {
                provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_XML, xmlProvider);
                provider.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE,
                        new Date(System.currentTimeMillis()).toString());
            }
        }
    }

    @Override
    public void updateCollection(Collection<?> collection) throws RepoxException {
        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
        updateProvider(client, collection.getProvider(), collection);

        String htypeString = collection.getValue(RepoxControlledVocabulary.HARVESTING_TYPE);
        DatasourceType harvestingtype = DatasourceType.oai_pmh;
        try {
            harvestingtype = DatasourceType.valueOf(htypeString);
        } catch (Throwable t){
            logger.log(Level.WARNING, "Failed to parse harvesting type: <" + harvestingtype + ">");
        }

        Source retDs = client.retrieveDataSourceByNameCode(collection.getMnemonic());
        String collectionId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (collectionId == null && retDs != null) {
            collectionId = retDs.getId();
            collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, retDs.getId());
        }

        // create the update ds for repox
        Source ds = xmlFactory.createDataSource(collection);

        // eather we dont have it in our map - so assume a new synch
        // or the thing does not exist in repox, but was synched before -
        // that happens when record is deleted in repox.
        if (collectionId == null || retDs == null) {

            eu.europeana.uim.repox.rest.client.xml.Provider jibxProv = new eu.europeana.uim.repox.rest.client.xml.Provider();
            jibxProv.setId(collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID));

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
        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
            client.deleteDatasource(id);
            collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, null);
        }
    }

    @Override
    public void synchronizeCollection(Collection<?> collection) throws RepoxException {
        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

            Source jaxbColl = client.retrieveDataSource(id);
            if (jaxbColl != null) {
                String xmlCollection;
                try {
                    xmlCollection = RepoxXmlUtils.marshall(jaxbColl);
                } catch (JAXBException e) {
                    throw new RuntimeException("Could not marshall source for id '" + id + "'!", e);
                }
                
                
                String storedXml = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_XML);
                if (storedXml == null || !storedXml.equals(xmlCollection)) {
                    collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_XML,
                            xmlCollection);
                    collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE,
                            new Date(System.currentTimeMillis()).toString());
                }
            }

            HarvestingStatus status = client.getHarvestingStatus(id);

            String storedStatus = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE);
            if ((storedStatus == null && status.getStatus() != null) ||
                (storedStatus != null && !storedStatus.equals(status.getStatus()))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE,
                        status.getStatus());
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE,
                        new Date(System.currentTimeMillis()).toString());
            }
            String storedRecords = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS);
            if ((storedRecords == null && status.getRecords() != null) ||
                (storedRecords != null && !storedRecords.equals(status.getRecords()))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS,
                        status.getRecords());
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE,
                        new Date(System.currentTimeMillis()).toString());
            }
        }
    }

    @Override
    public String getHarvestLog(Collection<?> collection) throws RepoxException {
        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return null; }

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

    @Override
    public List<String> getActiveHarvestings(String url) throws RepoxException {
        if (url == null || url.length() == 0) { return null; }

        RepoxRestClient client = clientfactory.getInstance(url);
        RunningTasks rTasks = client.getActiveHarvestingSessions();
        return rTasks.getDataSource();
    }
}
