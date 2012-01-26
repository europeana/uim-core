/* RestRepoxServiceTest.java - created on Jan 25, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import static org.mockito.Mockito.spy;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.repox.rest.client.RepoxRestClientFactoryImpl;
import eu.europeana.uim.repox.rest.client.RepoxRestClientTest;
import eu.europeana.uim.repox.rest.utils.BasicXmlObjectFactory;
import eu.europeana.uim.repox.rest.utils.RepoxControlledVocabulary;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.store.memory.MemoryStorageEngine;

/**
 * Tests functionality of service to Repox.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 25, 2012
 */
public class RepoxServiceTest {
    private MemoryStorageEngine engine;
    private RepoxService        service;
    private String              repoxUri;

    /**
     * Initialize repox service.
     * 
     * @throws Exception
     */
    @Before
    public void setupRepoxService() throws Exception {
        repoxUri = RepoxTestUtils.getUri(RepoxRestClientTest.class, "/config.properties");
        engine = spy(new MemoryStorageEngine());
        service = new RepoxServiceImpl(new RepoxRestClientFactoryImpl(),
                new BasicXmlObjectFactory());
    }

    /**
     * Tests creation, updating and deletion of provider in test repox installation.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateUpdateDeleteProvider() throws Exception {
        Provider<Long> provider = engine.createProvider();
        provider.setMnemonic("prov");
        provider.setName("provider");
        provider.setOaiBaseUrl(repoxUri);
        provider.putValue(StandardControlledVocabulary.COUNTRY, "de");
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
    }

    /**
     * Tests creation, updating and deletion of collection in test repox installation.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateUpdateDeleteCollection() throws Exception {
        Provider<Long> provider = engine.createProvider();
        provider.setMnemonic("prov");
        provider.setName("provider");
        provider.setOaiBaseUrl(repoxUri);
        provider.putValue(StandardControlledVocabulary.COUNTRY, "de");
        engine.updateProvider(provider);

        service.updateProvider(provider);

        Collection<Long> collection = engine.createCollection(provider);
        collection.setMnemonic("coll");
        collection.setName("collection");
        collection.setOaiBaseUrl(repoxUri);
        collection.putValue(StandardControlledVocabulary.COUNTRY, "de");
        engine.updateCollection(collection);

        service.updateCollection(collection);

        String aggrId = collection.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertNotNull(aggrId);

        String provId = collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertNotNull(provId);

        collection.setName("updated-collection");
        engine.updateCollection(collection);

        service.updateCollection(collection);

        String updAggrId = collection.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertEquals(aggrId, updAggrId);

        String updProvId = collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertEquals(provId, updProvId);

        service.synchronizeCollection(collection);
        Assert.assertNotNull(collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE));
        Assert.assertNotNull(collection.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS));

        Assert.assertNotNull(service.getHarvestLog(collection));

        service.deleteCollection(collection);

        aggrId = collection.getValue(RepoxControlledVocabulary.AGGREGATOR_REPOX_ID);
        Assert.assertNotNull(aggrId);

        provId = collection.getValue(RepoxControlledVocabulary.PROVIDER_REPOX_ID);
        Assert.assertNull(provId);

        service.deleteProvider(provider);
    }
}
