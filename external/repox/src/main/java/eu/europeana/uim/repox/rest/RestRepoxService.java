/* RepoxServiceImpl.java - created on Jan 23, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import eu.europeana.uim.external.ExternalServiceException;
import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.HarvestingState;
import eu.europeana.uim.repox.client.RepoxRestClient;
import eu.europeana.uim.repox.client.RepoxRestClientFactory;
import eu.europeana.uim.repox.rest.client.CompositeRepoxRestClientFactory;
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
public class RestRepoxService implements RepoxService {
    private static final Logger    log = Logger.getLogger(RestRepoxService.class.getName());

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
    public RestRepoxService() {
        this(new CompositeRepoxRestClientFactory(), new BasicXmlObjectFactory());
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
    public RestRepoxService(RepoxRestClientFactory factory, XmlObjectFactory xmlFactory) {
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
        log.log(Level.INFO, "Calling updateProvider for ''{0}''!", provider);

        if (provider.isAggregator()) { throw new RepoxException(
                "The requested object is not a Provider"); }
        if (provider.getOaiBaseUrl() == null || provider.getOaiBaseUrl().length() == 0) { return; }

        RepoxRestClient client = clientfactory.getInstance(provider.getOaiBaseUrl());
        updateProvider(client, provider, null);
    }

    private void updateProvider(RepoxRestClient client, Provider<?> provider,
            Collection<?> collection) throws RepoxException {
        String providerId;
        if (collection != null) {
            providerId = collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        } else {
            providerId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        }

        eu.europeana.uim.repox.rest.client.xml.Provider retProv = null;
        if (providerId != null) {
            retProv = client.retrieveProvider(providerId);
        }
        if (retProv == null) {
            providerId = null;
            retProv = client.retrieveProvider(provider.getMnemonic());

            if (retProv == null) {
                retProv = client.retrieveProviderByMetadata(provider.getMnemonic());
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
        }

        if (retProv == null) {
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = xmlFactory.createProvider(provider);
            jaxbProv.setId(provider.getMnemonic());

            String aggregatorId = updateAggregator(client, provider, collection);

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

        Aggregator rtAggregator = null;
        if (aggregatorId != null) {
            rtAggregator = client.retrieveAggregator(aggregator.getNameCode());
        }
        if (rtAggregator == null) {
            aggregatorId = null;

            rtAggregator = client.retrieveAggregator(aggregator.getNameCode());

            if (rtAggregator == null) {
                rtAggregator = client.retrieveAggregatorByMetadata(aggregator.getNameCode());
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
        log.log(Level.INFO, "Calling deleteProvider for ''{0}''!", provider);

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
        log.log(Level.INFO, "Calling synchronizeProvider for ''{0}''!", provider);

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

                log.log(Level.INFO, "Stored xml for ''{0}'' is ''{1}''!", new Object[]{provider, storedXml});
            }
        }
    }

    @Override
    public void updateCollection(Collection<?> collection) throws RepoxException {
        log.log(Level.INFO, "Calling updateCollection for ''{0}''!", collection);

        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

        updateProvider(client, collection.getProvider(), collection);

        String htypeString = collection.getValue(RepoxControlledVocabulary.HARVESTING_TYPE);
        DatasourceType harvestingtype = DatasourceType.OAI_PMH;
        try {
            htypeString = htypeString.replaceAll("[.]", "_");
            htypeString = htypeString.replaceAll("\\s", "_");
            harvestingtype = DatasourceType.valueOf(htypeString);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Failed to parse harvesting type: <{0}>", htypeString);
        }

        String collectionId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);

        Source retDs = null;
        if (collectionId != null) {
            retDs = client.retrieveDataSource(collectionId);
        }
        if (retDs == null) {
            collectionId = null;
            retDs = client.retrieveDataSource(collection.getMnemonic());

            if (retDs == null) {
                retDs = client.retrieveDataSourceByMetadata(collection.getMnemonic());
            }

            if (retDs != null) {
                collectionId = retDs.getId();
                collection.putValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID, retDs.getId());
            }
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

        log.log(Level.INFO, "Collection REPOX id is ''{0}''!", collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID));
        log.log(Level.INFO, "Provider REPOX id is ''{0}''!", collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID));
    }

    @Override
    public void deleteCollection(Collection<?> collection) throws RepoxException {
        log.log(Level.INFO, "Calling deleteCollection for ''{0}''!", collection);

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
        log.log(Level.INFO, "Calling synchronizeCollection for ''{0}''!", collection);

        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return; }

        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));

            String dateString = new Date(System.currentTimeMillis()).toString();

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
                    collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE, dateString);

                    log.log(Level.INFO, "Stored xml for ''{0}'' is ''{1}''!", new Object[]{collection, storedXml});
                }
            }

            HarvestingStatus status = client.getHarvestingStatus(id);

            String uimStatus = null;
            HarvestingState statusEnum = HarvestingState.valueOf(status.getStatus());
            if (statusEnum != null) {
                switch (statusEnum) {
                case CANCELED:
                    uimStatus = "Harvesting failed";
                    break;
                case ERROR:
                    uimStatus = "Harvesting failed";
                    break;
                case OK:
                    uimStatus = "Fully Harvested";
                    break;
                case RUNNING:
                    uimStatus = "Not harvested";
                    break;
                case WARNING:
                    uimStatus = "Not harvested";
                    break;
                case undefined:
                    break;
                }
            }

            String storedStatus = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE);
            if ((storedStatus == null && uimStatus != null) ||
                (storedStatus != null && !storedStatus.equals(uimStatus))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE,
                        uimStatus);
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE, dateString);

                log.log(Level.INFO, "Status for ''{0}'' is ''{1}''!", new Object[]{collection, uimStatus});
            }

            String storedRecords = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS);
            String records = client.retrieveRecordCount(id);

            if ((storedRecords == null && records != null) ||
                (storedRecords != null && !storedRecords.equals(records))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS, records);
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE, dateString);

                log.log(Level.INFO, "Number of records for ''{0}'' is ''{1}''!", new Object[]{collection, records});
            }

            String storedHarvestDate = collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_LAST_DATE);
            String harvestDate = client.retrieveLastIngestionDate(id);
            if (harvestDate != null) {
                String[] ds = harvestDate.split(" ");
                harvestDate = ds[0];
            }

            if ((storedHarvestDate == null && harvestDate != null) ||
                (storedHarvestDate != null && !storedHarvestDate.equals(harvestDate))) {
                collection.putValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_LAST_DATE,
                        harvestDate);
                collection.putValue(RepoxControlledVocabulary.LAST_UPDATE_DATE, dateString);

                log.log(Level.INFO, "Last harvesting date for ''{0}'' is ''{1}''!", new Object[]{collection, harvestDate});
            }
        } else {
            log.log(Level.WARNING, "Missing repox identifier for ''{0}''!", collection);
        }
    }

    @Override
    public String getHarvestLog(Collection<?> collection) throws RepoxException {
        log.log(Level.INFO, "Calling getHarvestLog for ''{0}''!", collection);

        if (collection.getOaiBaseUrl(true) == null || collection.getOaiBaseUrl(true).length() == 0) { return null; }

        StringBuilder sb = new StringBuilder();
        String id = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        if (id != null) {
            RepoxRestClient client = clientfactory.getInstance(collection.getOaiBaseUrl(true));
            Log harvestLog = client.getHarvestLog(id);

            List<String> linelist = harvestLog.getLine();
            for (String ln : linelist) {
                sb.append(ln);
            }

            log.log(Level.INFO, "Harvesting log for ''{0}'' is ''{1}''!", new Object[]{collection, sb.toString()});
        } else {
            log.log(Level.WARNING, "Missing repox identifier for ''{0}''!", collection);
        }

        return sb.toString();
    }

    @Override
    public List<String> getActiveHarvestings(String url) throws RepoxException {
        log.log(Level.INFO, "Calling getActiveHarvestings for ''{0}''!", url);

        if (url == null || url.length() == 0) { return null; }

        RepoxRestClient client = clientfactory.getInstance(url);
        RunningTasks rTasks = client.getActiveHarvestingSessions();
        return rTasks.getDataSource();
    }

    @Override
    public boolean synchronize(Provider<?> provider, boolean deleted)
            throws ExternalServiceException {
        boolean update = false;
        if (deleted) {
            deleteProvider(provider);
            update = true;
        } else {
            Map<String, String> beforeValues = new HashMap<>(provider.values());

            updateProvider(provider);
            synchronizeProvider(provider);

            Map<String, String> afterValues = provider.values();
            update = beforeValues.size() != afterValues.size();
            if (!update) {
                for (Entry<String, String> entry : afterValues.entrySet()) {
                    String beforeValue = beforeValues.get(entry.getKey());
                    if (!(beforeValue == null ? entry.getValue() == null
                            : beforeValue.equals(entry.getValue()))) {
                        update = true;
                        break;
                    }
                }
            }
        }
        return update;
    }

    @Override
    public boolean synchronize(Collection<?> collection, boolean deleted)
            throws ExternalServiceException {
        boolean update = false;
        if (deleted) {
            deleteCollection(collection);
            update = true;
        } else {
            Map<String, String> beforeValues = new HashMap<>(collection.values());

            updateCollection(collection);
            synchronizeCollection(collection);

            Map<String, String> afterValues = collection.values();

            update = beforeValues.size() != afterValues.size();
            if (!update) {
                for (Entry<String, String> entry : afterValues.entrySet()) {
                    String beforeValue = beforeValues.get(entry.getKey());
                    if (!(beforeValue == null ? entry.getValue() == null
                            : beforeValue.equals(entry.getValue()))) {
                        update = true;
                        break;
                    }
                }
            }
        }
        return update;
    }
}
