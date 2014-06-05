/* RestRepoxServiceTest.java - created on Jan 25, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import static org.mockito.Mockito.spy;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.client.RepoxRestClient;
import eu.europeana.uim.repox.rest.client.RepoxRestClientTest;
import eu.europeana.uim.repox.rest.client.CompositeRepoxRestClientFactory;
import eu.europeana.uim.repox.rest.client.xml.Source;
import eu.europeana.uim.repox.rest.utils.BasicXmlObjectFactory;
import eu.europeana.uim.repox.rest.utils.DatasourceType;
import eu.europeana.uim.storage.memory.MemoryStorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;

/**
 * Tests functionality of service to Repox.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 25, 2012
 */
public class RepoxServiceTest {
    private static MemoryStorageEngine             engine;
    private static RepoxService                    service;
    private static String                          repoxUri;
    private static String                          timeStamp;
    private static CompositeRepoxRestClientFactory factory;

    /**
     * Initialize repox service.
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setupRepoxService() throws Exception {
        repoxUri = RepoxTestUtils.getUri(RepoxRestClientTest.class, "/config.properties");
        engine = spy(new MemoryStorageEngine());
        factory = new CompositeRepoxRestClientFactory();
        service = new RestRepoxService(factory, new BasicXmlObjectFactory());
        timeStamp = Long.toString(System.nanoTime());
    }

    /**
     * Tests creation, updating and deletion of provider in test repox installation.
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCreateUpdateDeleteProvider() throws Exception {
        Provider<Long> provider = engine.createProvider();
        provider.setMnemonic("prov1_" + timeStamp);
        provider.setName("provider");
        provider.setOaiBaseUrl(repoxUri);
        provider.putValue(StandardControlledVocabulary.COUNTRY, "de_" + timeStamp);
        engine.updateProvider(provider);
        service.updateProvider(provider);

        String aggrId = provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertNotNull(aggrId);

        String provId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertNotNull(provId);

        provider.setName("updated-provider");
        engine.updateProvider(provider);
        service.updateProvider(provider);

        String updAggrId = provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertEquals(aggrId, updAggrId);

        String updProvId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertEquals(provId, updProvId);

        service.deleteProvider(provider);

        aggrId = provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertNotNull(aggrId);

        provId = provider.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertNull(provId);

        RepoxRestClient client = factory.getInstance(repoxUri);
        client.deleteAggregator(aggrId);
    }

    /**
     * Tests creation, updating and deletion of collection in test repox installation.
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCreateUpdateDeleteCollection() throws Exception {
        Provider<Long> provider = engine.createProvider();
        provider.setMnemonic("prov2_" + timeStamp);
        provider.setName("provider");
        provider.setOaiBaseUrl(repoxUri);
        provider.putValue(StandardControlledVocabulary.COUNTRY, "de_" + timeStamp);
        engine.updateProvider(provider);

        service.updateProvider(provider);

        Collection<Long> collection = engine.createCollection(provider);
        collection.setMnemonic("coll_" + timeStamp);
        collection.setName("collection");
        collection.setOaiBaseUrl(repoxUri);
        collection.putValue(StandardControlledVocabulary.COUNTRY, "de");
        collection.putValue(RepoxControlledVocabulary.HARVESTING_TYPE,
                DatasourceType.OAI_PMH.name());

        engine.updateCollection(collection);
        service.updateCollection(collection);

        Source source = factory.getInstance(repoxUri).retrieveDataSourceByMetadata(
                collection.getMnemonic());
        Assert.assertEquals(collection.getName(), source.getName());

        String collId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        Assert.assertNotNull(collId);

        collection.setName("updated-collection");
        engine.updateCollection(collection);
        service.updateCollection(collection);

        source = factory.getInstance(repoxUri).retrieveDataSourceByMetadata(
                collection.getMnemonic());
        Assert.assertEquals(collection.getName(), source.getName());

        String updCollId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        Assert.assertEquals(collId, updCollId);
        Assert.assertNotNull(collection.getValue(StandardControlledVocabulary.COUNTRY));

        service.synchronizeCollection(collection);
        Assert.assertNotNull(collection.getValue(StandardControlledVocabulary.COUNTRY));

        Assert.assertNotNull(collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE));
        Assert.assertNotNull(collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_LAST_DATE));
        Assert.assertNotNull(collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS));

        Assert.assertNotNull(service.getHarvestLog(collection));

        service.deleteCollection(collection);

        collId = collection.getValue(RepoxControlledVocabulary.COLLECTION_REPOX_ID);
        Assert.assertNull(collId);

        service.deleteProvider(provider);

        RepoxRestClient client = factory.getInstance(repoxUri);
        client.deleteAggregator(provider.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID));
    }
}
