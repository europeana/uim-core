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
    private static final Logger    log = Logger.getLogger(RepoxServiceImpl.class.getName());

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
        log.info("Calling updateProvider for '" + provider + "'!");

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

        eu.europeana.uim.repox.rest.client.xml.Provider retProv;
        if (providerId == null) {
            retProv = client.retrieveProvider(provider.getMnemonic());

            if (retProv == null) {
                retProv = client.retrieveProviderByNameCode(provider.getMnemonic());
            }

            if (retProv != null) {
                providerId = retProv.getId();

                if (collection != null) {
                    collection.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID,
                            retProv.getId());
                } else {
                    provider.putValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID, retProv.getId());
                }
            }
        } else {
            retProv = client.retrieveProvider(providerId);
        }

        if (retProv == null) {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            jaxbProv.setId(provider.getMnemonic());

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
            boolean update = xmlFactory.updateProvider(provider, retProv);
            if (update) {
                retProv.setId(providerId);
                client.updateProvider(retProv);
            }
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

        if (aggregatorId == null) {
            Aggregator rtAggregator = client.retrieveAggregator(aggregator.getNameCode());

            if (rtAggregator == null) {
                rtAggregator = client.retrieveAggregatorByNameCode(aggregator.getNameCode());
            }

            if (rtAggregator != null) {
                aggregatorId = rtAggregator.getId();

                if (collection != null) {
                    collection.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID,
                            rtAggregator.getId());
                } else {
                    provider.putValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID,
                            rtAggregator.getId());
                }
            }
        }

        if (aggregatorId == null) {
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
        log.info("Calling deleteProvider for '" + provider + "'!");

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
        log.info("Calling synchronizeProvider for '" + provider + "'!");

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

                log.info("Stored xml for '" + provider + "' is '" + storedXml + "'!");
            }
        }
    }

    @Override
    public void updateCollection(Collection<?> collection) throws RepoxException {
        log.info("Calling updateCollection for '" + collection + "'!");

        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

        updateProvider(client, collection.getProvider(), collection);

        String htypeString = collection.getValue(RepoxControlledVocabulary.HARVESTING_TYPE);
        DatasourceType harvestingtype = DatasourceType.OAI_PMH;
        try {
            htypeString = htypeString.replaceAll("[.]", "_");
            harvestingtype = DatasourceType.valueOf(htypeString);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Failed to parse harvesting type: <" + htypeString + ">");
        }

        String collectionId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);

        Source retDs;
        if (collectionId == null) {
            retDs = client.retrieveDataSource(collection.getMnemonic());

            if (retDs == null) {
                retDs = client.retrieveDataSourceByNameCode(collection.getMnemonic());
            }

            if (retDs != null) {
                collectionId = retDs.getId();
                collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, retDs.getId());
            }
        } else {
            retDs = client.retrieveDataSource(collectionId);
        }

        if (retDs == null) {
            Source ds = xmlFactory.createDataSource(collection);

            eu.europeana.uim.repox.rest.client.xml.Provider jibxProv = new eu.europeana.uim.repox.rest.client.xml.Provider();
            jibxProv.setId(collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID));

            Source retsource = null;
            switch (harvestingtype) {
            case OAI_PMH:
                retsource = client.createDatasourceOAI(ds, jibxProv);
                break;
            case Z39_50:
                retsource = client.createDatasourceZ3950Timestamp(ds, jibxProv);
                break;
            case FTP:
                retsource = client.createDatasourceFtp(ds, jibxProv);
                break;
            case HTTP:
                retsource = client.createDatasourceHttp(ds, jibxProv);
                break;
            case FILE:
                retsource = client.createDatasourceFolder(ds, jibxProv);
                break;
            }

            if (retsource == null) { throw new RepoxException(
                    "Could not create source for collection '" + collection.getMnemonic() + "'!"); }

            collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, retsource.getId());
        } else {
            boolean update = xmlFactory.updateDataSource(collection, retDs);

            if (update) {
                Source retsource = null;
                switch (harvestingtype) {
                case OAI_PMH:
                    retsource = client.updateDatasourceOAI(retDs);
                    break;
                case Z39_50:
                    retsource = client.updateDatasourceZ3950Timestamp(retDs);
                    break;
                case FTP:
                    retsource = client.updateDatasourceFtp(retDs);
                    break;
                case HTTP:
                    retsource = client.updateDatasourceHttp(retDs);
                    break;
                case FILE:
                    retsource = client.updateDatasourceFolder(retDs);
                    break;
                }

                if (retsource == null) { throw new RepoxException(
                        "Could not update source for collection '" + collection.getMnemonic() +
                                "'!"); }
            }
        }

        log.info("Collection REPOX id is '" +
                 collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID) + "'!");
        log.info("Provider REPOX id is '" +
                 collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_XML) + "'!");
    }

    @Override
    public void deleteCollection(Collection<?> collection) throws RepoxException {
        log.info("Calling deleteCollection for '" + collection + "'!");

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
        log.info("Calling synchronizeCollection for '" + collection + "'!");

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

                log.info("Status for '" + collection + "' is '" + storedStatus + "'!");
            }

            String storedRecords = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS);
            if ((storedRecords == null && status.getRecords() != null) ||
                (storedRecords != null && !storedRecords.equals(status.getRecords()))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS,
                        status.getRecords());
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE,
                        new Date(System.currentTimeMillis()).toString());

                log.info("Status for '" + collection + "' is '" + storedRecords + "'!");
            }
        } else {
            log.warning("Missing repox identifier for '" + collection + "'!");
        }
    }

    @Override
    public String getHarvestLog(Collection<?> collection) throws RepoxException {
        log.info("Calling getHarvestLog for '" + collection + "'!");

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

            log.info("Harvesting log for '" + collection + "' is '" + harvestLog + "'!");
        } else {
            log.warning("Missing repox identifier for '" + collection + "'!");
        }

        return sb.toString();
    }

    @Override
    public List<String> getActiveHarvestings(String url) throws RepoxException {
        log.info("Calling getActiveHarvestings for '" + url + "'!");

        if (url == null || url.length() == 0) { return null; }

        RepoxRestClient client = clientfactory.getInstance(url);
        RunningTasks rTasks = client.getActiveHarvestingSessions();
        return rTasks.getDataSource();
    }
}
