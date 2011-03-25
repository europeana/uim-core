package eu.europeana.uim.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;

/**
 * Abstract, base class for all {@link StorageEngine} implementations. It tests creation, update and
 * retrieval for all kinds of data sets (Provider, Collection, Request, MetadataRecord).
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public abstract class AbstractStorageEngineTest<I> {
    StorageEngine<I> engine = null;

    private enum TestEnum {
        EN;
    }

    /**
     * Setups storage engine.
     */
    @Before
    public void setUp() {
        engine = getStorageEngine();
        performSetUp();
    }

    /**
     * Override this for additional setup
     */
    protected void performSetUp() {
        // nothing todo
    }

    /**
     * @return configured storage engine
     */
    protected abstract StorageEngine<I> getStorageEngine();

    /**
     * Tests creation of providers and that retrieval of it still contains all given information.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testCreateAndGetProvider() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        provider0.setAggregator(true);
        provider0.setOaiBaseUrl("http://oai.base");
        provider0.setOaiMetadataPrefix("oai_dc");
        engine.updateProvider(provider0);

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        Provider<I> provider2 = engine.getProvider(provider0.getId());
        assertEquals(provider2.getMnemonic(), provider0.getMnemonic());
        assertEquals(provider2.getName(), provider0.getName());
        assertEquals(provider2.isAggregator(), provider0.isAggregator());
        assertEquals(provider2.getOaiBaseUrl(), provider0.getOaiBaseUrl());
        assertEquals(provider2.getOaiMetadataPrefix(), provider0.getOaiMetadataPrefix());
    }

    /**
     * Tests the find functionality for providers and that the retrieved provider is a correct one.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testFindProvider() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        provider0.setAggregator(true);
        provider0.setOaiBaseUrl("http://oai.base");
        provider0.setOaiMetadataPrefix("oai_dc");
        engine.updateProvider(provider0);

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        Provider<I> provider2 = engine.findProvider("TEL");
        assertEquals(provider2.getMnemonic(), provider0.getMnemonic());
        assertEquals(provider2.getName(), provider0.getName());
        assertEquals(provider2.isAggregator(), provider0.isAggregator());
        assertEquals(provider2.getOaiBaseUrl(), provider0.getOaiBaseUrl());
        assertEquals(provider2.getOaiMetadataPrefix(), provider0.getOaiMetadataPrefix());
    }

    /**
     * Tests the correct exception handling in case of a duplicate provider.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testCreateDuplicateProvider() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        provider0.setAggregator(true);
        provider0.setOaiBaseUrl("http://oai.base");
        provider0.setOaiMetadataPrefix("oai_dc");
        engine.updateProvider(provider0);

        Provider<I> provider1;
        try {
            provider1 = engine.createProvider();
            provider1.setMnemonic("TEL");
            provider1.setName("The European Library - DUPLICATE");
            engine.updateProvider(provider1);

            fail("Duplicate mnemonci is not allowed.");
        } catch (Exception e) {
        }
    }

    /**
     * Tests relations between roviders.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testRelationProviderProvider() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        provider0.getRelatedOut().add(provider1);
        engine.updateProvider(provider0);

        // //engine.checkpoint();

        Provider<I> provider2 = engine.getProvider(provider1.getId());
        assertEquals(1, provider2.getRelatedIn().size());
        assertEquals(provider0.getMnemonic(), (provider2.getRelatedIn().iterator().next()).getMnemonic());

        Provider<I> provider3 = engine.getProvider(provider0.getId());
        assertEquals(1, provider3.getRelatedOut().size());
        assertEquals(provider1.getMnemonic(), provider3.getRelatedOut().iterator().next().getMnemonic());
    }

    /**
     * Tests creation and retrieval of collections.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testCreateAndGetCollection() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        collection0.setOaiBaseUrl("http://oai.base");
        collection0.setOaiMetadataPrefix("oai_dc");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Collection<I> collection2 = engine.createCollection(provider1);
        collection2.setMnemonic("a0003");
        collection2.setName("BNF's collection 001");
        engine.updateCollection(collection2);
        // //engine.checkpoint();

        Collection<I> collection3 = engine.getCollection(collection0.getId());
        assertEquals(collection3.getMnemonic(), collection0.getMnemonic());
        assertEquals(collection3.getName(), collection0.getName());
        assertEquals(collection3.getOaiBaseUrl(), collection0.getOaiBaseUrl());
        assertEquals(collection3.getOaiMetadataPrefix(), collection0.getOaiMetadataPrefix());

        assertEquals(collection3.getProvider().getMnemonic(), provider0.getMnemonic());

        Collection<I> collection4 = engine.getCollection(collection2.getId());
        assertEquals(collection4.getMnemonic(), collection2.getMnemonic());
        assertEquals(collection4.getName(), collection2.getName());

        assertEquals(collection4.getProvider().getMnemonic(), provider1.getMnemonic());
    }

    /**
     * Tests creation of duplicate collections.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testCreateDuplicateCollection() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Collection<I> collection1;
        try {
            collection1 = engine.createCollection(provider0);
            collection1.setMnemonic("a0001");
            collection1.setName("TEL's collection 001 - DUPLICATE");
            engine.updateCollection(collection1);

            fail("Duplicate mnemonci is not allowed.");
        } catch (Exception e) {
        }
    }

    /**
     * Tests relation between provider and collection.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testRelationProviderCollection() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        collection0.setOaiBaseUrl("http://oai.base");
        collection0.setOaiMetadataPrefix("oai_dc");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Collection<I> collection2 = engine.createCollection(provider0);
        collection2.setMnemonic("a0003");
        collection2.setName("TEL's collection 003");
        engine.updateCollection(collection2);
        // //engine.checkpoint();

        List<Collection<I>> collections = engine.getCollections(provider0);
        assertEquals(3, collections.size());
        for (Collection<I> collection : collections) {
            assertNotNull(collection.getMnemonic());
            assertNotNull(collection.getName());
            assertNotNull(collection.getProvider());
            assertEquals(collection.getProvider().getMnemonic(), provider0.getMnemonic());
        }
    }

    /**
     * Tests retrieval of collections.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testFindCollection() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Collection<I> collection2 = engine.createCollection(provider0);
        collection2.setMnemonic("a0003");
        collection2.setName("TEL's collection 003");
        engine.updateCollection(collection2);

        Collection<I> collection3 = engine.findCollection("a0002");
        assertEquals(collection3.getMnemonic(), collection1.getMnemonic());
        assertEquals(collection3.getName(), collection1.getName());
    }

    /**
     * Tests creation and retrieval of requests.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testCreateAndGetRequest() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Request<I> request1 = engine.createRequest(collection0, new Date(1000));
        engine.updateRequest(request1);

        Request<I> request2 = engine.createRequest(collection1, new Date(2000));
        engine.updateRequest(request2);
        // //engine.checkpoint();

        Request<I> request3 = engine.getRequest(request0.getId());
        assertNotNull(request3.getCollection());
        assertNotNull(request3.getDate());
        assertEquals(request3.getCollection().getMnemonic(), collection0.getMnemonic());
    }

    /**
     * Tests not allowed creation of duplicate requests.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testCreateDuplicateRequest() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        try {
            Request<I> request1 = engine.createRequest(collection0, new Date(0));
            engine.updateRequest(request1);

            fail("Duplicate request (within same second) is not allowed.");
        } catch (Exception e) {
        }
    }

    /**
     * Tests relation between collection and request.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testRelationCollectionRequest() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Request<I> request1 = engine.createRequest(collection0, new Date(1000));
        engine.updateRequest(request1);
        // engine.checkpoint();

        Request<I> request2 = engine.getRequest(request0.getId());
        assertNotNull(request2.getCollection());
        assertEquals(request2.getCollection().getMnemonic(), collection0.getMnemonic());

        Request<I> request3 = engine.getRequest(request1.getId());
        assertNotNull(request3.getCollection());
        assertEquals(request3.getCollection().getMnemonic(), collection0.getMnemonic());
    }

    /**
     * Tests creation and retrieval of executions.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testCreateAndGetExecution() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Execution<I> execution0 = engine.createExecution(provider0, "provider");
        engine.updateExecution(execution0);
        Execution<I> execution1 = engine.createExecution(collection0, "collection");
        engine.updateExecution(execution1);
        Execution<I> execution2 = engine.createExecution(request0, "request");
        engine.updateExecution(execution2);

        Execution<I> execution3 = engine.getExecution(execution0.getId());
        assertNotNull(execution3.getDataSet());
        assertEquals("provider", execution3.getWorkflowName());
        assertEquals(execution3.getDataSet().getId(), provider0.getId());

        Execution<I> execution4 = engine.getExecution(execution1.getId());
        assertNotNull(execution4.getDataSet());
        assertEquals("collection", execution4.getWorkflowName());
        assertEquals(execution4.getDataSet().getId(), collection0.getId());

        Execution<I> execution5 = engine.getExecution(execution2.getId());
        assertNotNull(execution5.getDataSet());
        assertEquals("request", execution5.getWorkflowName());
        assertEquals(execution5.getDataSet().getId(), request0.getId());
    }

    /**
     * Tests creation and retrieval of records.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testCreateAndGetRecord() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Set<Enum<?>> qualifiers = new HashSet<Enum<?>>() { { add(TestEnum.EN); } };
        
        MetaDataRecord<I> record0 = engine.createMetaDataRecord(request0, "abcd");
        record0.addField(MDRFieldRegistry.title, "title 01");
        record0.addField(MDRFieldRegistry.title, "title 02");
        record0.addQField(MDRFieldRegistry.title, "title 03", qualifiers);

        record0.addField(MDRFieldRegistry.rawformat, "MARC21");
        engine.updateMetaDataRecord(record0);

        MetaDataRecord<I> record3 = engine.getMetaDataRecord(record0.getId());
        assertEquals("title 01", record3.getFirstField(MDRFieldRegistry.title));
        assertEquals("title 03", record3.getQField(MDRFieldRegistry.title, qualifiers).get(0));

        assertEquals(3, record3.getField(MDRFieldRegistry.title).size());
    }

    /**
     * Tests creation of duplicate records.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testCreateDuplicateRecord() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Request<I> request1 = engine.createRequest(collection1, new Date(0));
        engine.updateRequest(request1);

        MetaDataRecord<I> record0 = engine.createMetaDataRecord(request0, "abcd");
        record0.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record0);

        try {
            MetaDataRecord<I> record1 = engine.createMetaDataRecord(request0, "abcd");
            record1.addField(MDRFieldRegistry.title, "title 01");
            engine.updateMetaDataRecord(record1);
        } catch (Exception e) {
            fail("Duplicate record with same identifier for provider should be allowed.");
        }

        try {
            MetaDataRecord<I> record2 = engine.createMetaDataRecord(request1, "abcd");
            record2.addField(MDRFieldRegistry.title, "title 01");
            engine.updateMetaDataRecord(record2);
        } catch (Exception e) {
            fail("Duplicate record with same identifier for provider should be allowed even in different collections.");
        }

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        Collection<I> collection2 = engine.createCollection(provider1);
        collection2.setMnemonic("a0003");
        collection2.setName("BNF's collection 001");
        engine.updateCollection(collection2);

        Request<I> request2 = engine.createRequest(collection2, new Date(0));
        engine.updateRequest(request2);

        // same identifier for different providers is ok.
        MetaDataRecord<I> record2 = engine.createMetaDataRecord(request2, "abcd");
        record2.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record2);
    }

    /**
     * Tests relation between request and record.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testRelationRequestRecord() throws StorageEngineException, InterruptedException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Provider<I> provider1 = engine.createProvider();
        provider1.setMnemonic("BNF");
        provider1.setName("French National Library");
        engine.updateProvider(provider1);

        provider0.getRelatedOut().add(provider1);
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Collection<I> collection1 = engine.createCollection(provider0);
        collection1.setMnemonic("a0002");
        collection1.setName("TEL's collection 002");
        engine.updateCollection(collection1);

        Collection<I> collection2 = engine.createCollection(provider1);
        collection2.setMnemonic("a0003");
        collection2.setName("BNF's collection 002");
        engine.updateCollection(collection2);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        Request<I> request1 = engine.createRequest(collection0, new Date(1000));
        engine.updateRequest(request1);

        Request<I> request2 = engine.createRequest(collection1, new Date(0));
        engine.updateRequest(request2);

        Request<I> request3 = engine.createRequest(collection1, new Date(1000));
        engine.updateRequest(request3);

        Request<I> request4 = engine.createRequest(collection2, new Date(1000));
        engine.updateRequest(request4);

        MetaDataRecord<I> record0 = engine.createMetaDataRecord(request0, "abcd0");
        record0.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record0);

        MetaDataRecord<I> record1 = engine.createMetaDataRecord(request0, "abcd1");
        record1.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record1);

        MetaDataRecord<I> record2 = engine.createMetaDataRecord(request0, "abcd2");
        record2.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record2);

        MetaDataRecord<I> record3 = engine.createMetaDataRecord(request1, "abcd3");
        record3.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record3);

        MetaDataRecord<I> record4 = engine.createMetaDataRecord(request2, "abcd4");
        record4.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record4);

        MetaDataRecord<I> record5 = engine.createMetaDataRecord(request3, "abcd5");
        record5.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record5);

        MetaDataRecord<I> record6 = engine.createMetaDataRecord(request4, "abcd6");
        record6.addField(MDRFieldRegistry.title, "title 01");
        engine.updateMetaDataRecord(record6);

        // validate by request
        I[] ids = engine.getByRequest(request0);
        assertEquals(3, ids.length);

        Set<I> idSet = new HashSet<I>();
        idSet.addAll(Arrays.asList(ids));

        assertTrue(idSet.contains(record0.getId()));
        assertTrue(idSet.contains(record1.getId()));
        assertTrue(idSet.contains(record2.getId()));

        assertFalse(idSet.contains(record5.getId()));

        // validate by collection
        ids = engine.getByCollection(collection0);
        assertEquals(4, ids.length);

        idSet = new HashSet<I>();
        idSet.addAll(Arrays.asList(ids));

        assertTrue(idSet.contains(record0.getId()));
        assertTrue(idSet.contains(record1.getId()));
        assertTrue(idSet.contains(record2.getId()));
        assertTrue(idSet.contains(record3.getId()));

        // validate by provider
        ids = engine.getByProvider(provider0, false);
        assertEquals(6, ids.length);

        idSet = new HashSet<I>();
        idSet.addAll(Arrays.asList(ids));

        assertTrue(idSet.contains(record0.getId()));
        assertTrue(idSet.contains(record1.getId()));
        assertTrue(idSet.contains(record2.getId()));
        assertTrue(idSet.contains(record3.getId()));
        assertTrue(idSet.contains(record4.getId()));
        assertTrue(idSet.contains(record5.getId()));

        // validate by provider
        ids = engine.getByProvider(provider0, true);
        assertEquals(7, ids.length);

        idSet = new HashSet<I>();
        idSet.addAll(Arrays.asList(ids));

        assertTrue(idSet.contains(record0.getId()));
        assertTrue(idSet.contains(record1.getId()));
        assertTrue(idSet.contains(record2.getId()));
        assertTrue(idSet.contains(record3.getId()));
        assertTrue(idSet.contains(record4.getId()));
        assertTrue(idSet.contains(record5.getId()));
        assertTrue(idSet.contains(record6.getId()));

        // validate all ids.
        I[] allIds = engine.getAllIds();
        assertEquals(7, allIds.length);

        idSet = new HashSet<I>();
        idSet.addAll(Arrays.asList(allIds));
        assertTrue(idSet.contains(record0.getId()));
        assertTrue(idSet.contains(record1.getId()));
        assertTrue(idSet.contains(record2.getId()));
        assertTrue(idSet.contains(record3.getId()));
        assertTrue(idSet.contains(record4.getId()));
        assertTrue(idSet.contains(record5.getId()));
        assertTrue(idSet.contains(record6.getId()));
    }
}
