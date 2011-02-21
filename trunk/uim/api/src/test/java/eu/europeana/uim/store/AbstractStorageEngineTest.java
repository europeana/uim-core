package eu.europeana.uim.store;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractStorageEngineTest {

    StorageEngine engine = null;

    @Before
    public void setUp() {
        engine = getStorageEngine();
        performSetUp();
    }

    /**
     * Override this for additional setup
     */
    protected void performSetUp() {

    }

	protected abstract StorageEngine getStorageEngine();
	
	@Test
	public void testCreateAndGetProvider() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		provider0.setAggregator(true);
		provider0.setOaiBaseUrl("http://oai.base");
		provider0.setOaiMetadataPrefix("oai_dc");
		engine.updateProvider(provider0);
		
		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);
		
		Provider provider2 = engine.getProvider(provider0.getId());
		assertEquals(provider2.getMnemonic(), provider0.getMnemonic());
		assertEquals(provider2.getName(), provider0.getName());
		assertEquals(provider2.isAggregator(), provider0.isAggregator());
		assertEquals(provider2.getOaiBaseUrl(), provider0.getOaiBaseUrl());
		assertEquals(provider2.getOaiMetadataPrefix(), provider0.getOaiMetadataPrefix());
	}

	@Test
	public void testFindProvider() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		provider0.setAggregator(true);
		provider0.setOaiBaseUrl("http://oai.base");
		provider0.setOaiMetadataPrefix("oai_dc");
		engine.updateProvider(provider0);
		
		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);

		Provider provider2 = engine.findProvider("TEL");
		assertEquals(provider2.getMnemonic(), provider0.getMnemonic());
		assertEquals(provider2.getName(), provider0.getName());
		assertEquals(provider2.isAggregator(), provider0.isAggregator());
		assertEquals(provider2.getOaiBaseUrl(), provider0.getOaiBaseUrl());
		assertEquals(provider2.getOaiMetadataPrefix(), provider0.getOaiMetadataPrefix());
	}

	@Test
	public void testCreateDuplicateProvider() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		provider0.setAggregator(true);
		provider0.setOaiBaseUrl("http://oai.base");
		provider0.setOaiMetadataPrefix("oai_dc");
		engine.updateProvider(provider0);

		Provider provider1;
		try {
			provider1 = engine.createProvider();
			provider1.setMnemonic("TEL");
			provider1.setName("The European Library - DUPLICATE");
			engine.updateProvider(provider1);
			
			fail("Duplicate mnemonci is not allowed.");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testRelationProviderProvider() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);

		provider0.getRelatedOut().add(provider1);
		engine.updateProvider(provider0);
		
		engine.checkpoint();
		
		Provider provider2 = engine.getProvider(provider1.getId());
		assertEquals(1, provider2.getRelatedIn().size());
		assertEquals(provider0.getMnemonic(), provider2.getRelatedIn().get(0).getMnemonic());
		
		Provider provider3 = engine.getProvider(provider0.getId());
		assertEquals(1, provider3.getRelatedOut().size());
		assertEquals(provider1.getMnemonic(), provider3.getRelatedOut().get(0).getMnemonic());
		
	}

	
	@Test
	public void testCreateAndGetCollection() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		collection0.setOaiBaseUrl("http://oai.base");
		collection0.setOaiMetadataPrefix("oai_dc");
		engine.updateCollection(collection0);
		
		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Collection collection2 = engine.createCollection(provider1);
		collection2.setMnemonic("a0003");
		collection2.setName("BNF's collection 001");
		engine.updateCollection(collection2);
		engine.checkpoint();

		Collection collection3 = engine.getCollection(collection0.getId());
		assertEquals(collection3.getMnemonic(), collection0.getMnemonic());
		assertEquals(collection3.getName(), collection0.getName());
		assertEquals(collection3.getOaiBaseUrl(), collection0.getOaiBaseUrl());
		assertEquals(collection3.getOaiMetadataPrefix(), collection0.getOaiMetadataPrefix());

		assertEquals(collection3.getProvider().getMnemonic(), provider0.getMnemonic());

		Collection collection4 = engine.getCollection(collection2.getId());
		assertEquals(collection4.getMnemonic(), collection2.getMnemonic());
		assertEquals(collection4.getName(), collection2.getName());

		assertEquals(collection4.getProvider().getMnemonic(), provider1.getMnemonic());
	}

	
	@Test
	public void testCreateDuplicateCollection() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);

		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);

		Collection collection1;
		try {
			collection1 = engine.createCollection(provider0);
			collection1.setMnemonic("a0001");
			collection1.setName("TEL's collection 001 - DUPLICATE");
			engine.updateCollection(collection1);
			
			fail("Duplicate mnemonci is not allowed.");
		} catch (Exception e) {
		}
	}
	

	
	@Test
	public void testRelationProviderCollection() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		collection0.setOaiBaseUrl("http://oai.base");
		collection0.setOaiMetadataPrefix("oai_dc");
		engine.updateCollection(collection0);
		
		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Collection collection2 = engine.createCollection(provider0);
		collection2.setMnemonic("a0003");
		collection2.setName("TEL's collection 003");
		engine.updateCollection(collection2);
		engine.checkpoint();

		List<Collection> collections = engine.getCollections(provider0);
		assertEquals(3, collections.size());
		for (Collection collection : collections) {
			assertNotNull(collection.getMnemonic());
			assertNotNull(collection.getName());
			assertNotNull(collection.getProvider());
			assertEquals(collection.getProvider().getMnemonic(), provider0.getMnemonic());
		}
	}

	@Test
	public void testFindCollection() throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);

		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Collection collection2 = engine.createCollection(provider0);
		collection2.setMnemonic("a0003");
		collection2.setName("TEL's collection 003");
		engine.updateCollection(collection2);

		Collection collection3 = engine.findCollection("a0002");
		assertEquals(collection3.getMnemonic(), collection1.getMnemonic());
		assertEquals(collection3.getName(), collection1.getName());
	}
	
	
	
	@Test
	public void testCreateAndGetRequest() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);
		
		Request request1 = engine.createRequest(collection0, new Date(1000));
		engine.updateRequest(request1);
		
		Request request2 = engine.createRequest(collection1, new Date(2000));
		engine.updateRequest(request2);
		engine.checkpoint();
		
		Request request3 = engine.getRequest(request0.getId());
		assertNotNull(request3.getCollection());
		assertNotNull(request3.getDate());
		assertEquals(request3.getCollection().getMnemonic(), collection0.getMnemonic());
	}

	@Test
	public void testCreateDuplicateRequest() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);
	
		try {
			Request request1 = engine.createRequest(collection0, new Date(0));
			engine.updateRequest(request1);

			fail("Duplicate request (within same second) is not allowed.");
		} catch (Exception e) {
		}
	}

	
	@Test
	public void testRelationCollectionRequest() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);
		
		Request request1 = engine.createRequest(collection0, new Date(1000));
		engine.updateRequest(request1);
		engine.checkpoint();
		
		Request request2 = engine.getRequest(request0.getId());
		assertNotNull(request2.getCollection());
		assertEquals(request2.getCollection().getMnemonic(), collection0.getMnemonic());

		Request request3 = engine.getRequest(request1.getId());
		assertNotNull(request3.getCollection());
		assertEquals(request3.getCollection().getMnemonic(), collection0.getMnemonic());
	}
	
	
	@Test
	public void testCreateAndGetExecution() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);

		Execution execution0 = engine.createExecution(provider0, "provider");
		engine.updateExecution(execution0);
		Execution execution1 = engine.createExecution(collection0, "collection");
		engine.updateExecution(execution1);
		Execution execution2 = engine.createExecution(request0, "request");
		engine.updateExecution(execution2);
		
		
		Execution execution3 = engine.getExecution(execution0.getId());
		assertNotNull(execution3.getDataSet());
		assertEquals("provider", execution3.getWorkflowName());
		assertEquals(execution3.getDataSet().getId(), provider0.getId());

		Execution execution4 = engine.getExecution(execution1.getId());
		assertNotNull(execution4.getDataSet());
		assertEquals("collection", execution4.getWorkflowName());
		assertEquals(execution4.getDataSet().getId(), collection0.getId());

		Execution execution5 = engine.getExecution(execution2.getId());
		assertNotNull(execution5.getDataSet());
		assertEquals("request", execution5.getWorkflowName());
		assertEquals(execution5.getDataSet().getId(), request0.getId());
	}
	
	
	@Test
	public void testCreateAndGetRecord() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);

		MetaDataRecord record0 = engine.createMetaDataRecord(request0, "abcd");
		record0.addField(MDRFieldRegistry.title, "title 01");
		record0.addField(MDRFieldRegistry.title, "title 02");
		record0.addQField(MDRFieldRegistry.title, "EN", "title 03");
		
		record0.setFirstField(MDRFieldRegistry.rawformat, "MARC21");
		engine.updateMetaDataRecord(record0);
		
		MetaDataRecord record3 = engine.getMetaDataRecords(record0.getId())[0];
		assertEquals("title 01", record3.getFirstField(MDRFieldRegistry.title));
		assertEquals("title 03", record3.getQField(MDRFieldRegistry.title, "EN").get(0));

		assertEquals(3, record3.getField(MDRFieldRegistry.title).size());
	}
	
	@Test
	public void testCreateDuplicateRecord() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);
		
		Request request1 = engine.createRequest(collection1, new Date(0));
		engine.updateRequest(request1);

		MetaDataRecord record0 = engine.createMetaDataRecord(request0, "abcd");
		record0.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record0);

		
		try {
			MetaDataRecord record1 = engine.createMetaDataRecord(request0, "abcd");
			record1.addField(MDRFieldRegistry.title, "title 01");
			engine.updateMetaDataRecord(record1);

			fail("Duplicate record with same identifier for provider is not allowed.");
		} catch (Exception e) {
		}

		try {
			MetaDataRecord record2 = engine.createMetaDataRecord(request1, "abcd");
			record2.addField(MDRFieldRegistry.title, "title 01");
			engine.updateMetaDataRecord(record2);

			fail("Duplicate record with same identifier for provider is not allowed even in different collections.");
		} catch (Exception e) {
		}

		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);
		
		Collection collection2 = engine.createCollection(provider1);
		collection2.setMnemonic("a0003");
		collection2.setName("BNF's collection 001");
		engine.updateCollection(collection2);
		
		Request request2 = engine.createRequest(collection2, new Date(0));
		engine.updateRequest(request2);
		
		//same identifier for different providers is ok.
		MetaDataRecord record2 = engine.createMetaDataRecord(request2, "abcd");
		record2.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record2);
	}

	
	@Test
	public void testRelationRequestRecord() throws StorageEngineException, InterruptedException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Provider provider1 = engine.createProvider();
		provider1.setMnemonic("BNF");
		provider1.setName("French National Library");
		engine.updateProvider(provider1);

		provider0.getRelatedOut().add(provider1);
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Collection collection1 = engine.createCollection(provider0);
		collection1.setMnemonic("a0002");
		collection1.setName("TEL's collection 002");
		engine.updateCollection(collection1);
		
		Collection collection2 = engine.createCollection(provider1);
		collection2.setMnemonic("a0003");
		collection2.setName("BNF's collection 002");
		engine.updateCollection(collection2);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);

		Request request1 = engine.createRequest(collection0, new Date(1000));
		engine.updateRequest(request1);
		
		Request request2 = engine.createRequest(collection1, new Date(0));
		engine.updateRequest(request2);
		
		Request request3 = engine.createRequest(collection1, new Date(1000));
		engine.updateRequest(request3);
		
		Request request4 = engine.createRequest(collection2, new Date(1000));
		engine.updateRequest(request4);
		

		MetaDataRecord record0 = engine.createMetaDataRecord(request0, "abcd0");
		record0.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record0);

		MetaDataRecord record1 = engine.createMetaDataRecord(request0, "abcd1");
		record1.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record1);

		MetaDataRecord record2 = engine.createMetaDataRecord(request0, "abcd2");
		record2.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record2);

		MetaDataRecord record3 = engine.createMetaDataRecord(request1, "abcd3");
		record3.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record3);
		
		MetaDataRecord record4 = engine.createMetaDataRecord(request2, "abcd4");
		record4.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record4);
		
		MetaDataRecord record5 = engine.createMetaDataRecord(request3, "abcd5");
		record5.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record5);
		
		MetaDataRecord record6 = engine.createMetaDataRecord(request4, "abcd6");
		record6.addField(MDRFieldRegistry.title, "title 01");
		engine.updateMetaDataRecord(record6);

		// validate by request
		long[] ids = engine.getByRequest(request0);
		assertEquals(3, ids.length);
		
		Set<Long> idSet = new HashSet<Long>();
		idSet.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
		
		assertTrue(idSet.contains(record0.getId()));
		assertTrue(idSet.contains(record1.getId()));
		assertTrue(idSet.contains(record2.getId()));
		
		assertFalse(idSet.contains(record5.getId()));

		
		// validate by collection
		ids = engine.getByCollection(collection0);
		assertEquals(4, ids.length);
		
		idSet = new HashSet<Long>();
		idSet.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
		
		assertTrue(idSet.contains(record0.getId()));
		assertTrue(idSet.contains(record1.getId()));
		assertTrue(idSet.contains(record2.getId()));
		assertTrue(idSet.contains(record3.getId()));

		
		// validate by provider
		ids = engine.getByProvider(provider0, false);
		assertEquals(6, ids.length);
		
		idSet = new HashSet<Long>();
		idSet.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
		
		assertTrue(idSet.contains(record0.getId()));
		assertTrue(idSet.contains(record1.getId()));
		assertTrue(idSet.contains(record2.getId()));
		assertTrue(idSet.contains(record3.getId()));
		assertTrue(idSet.contains(record4.getId()));
		assertTrue(idSet.contains(record5.getId()));

		// validate by provider
		ids = engine.getByProvider(provider0, true);
		assertEquals(7, ids.length);
		
		idSet = new HashSet<Long>();
		idSet.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
		
		assertTrue(idSet.contains(record0.getId()));
		assertTrue(idSet.contains(record1.getId()));
		assertTrue(idSet.contains(record2.getId()));
		assertTrue(idSet.contains(record3.getId()));
		assertTrue(idSet.contains(record4.getId()));
		assertTrue(idSet.contains(record5.getId()));
		assertTrue(idSet.contains(record6.getId()));

		// validate all ids.
		long[] allIds = engine.getAllIds();
		assertEquals(7, allIds.length);

		idSet = new HashSet<Long>();
		idSet.addAll(Arrays.asList(ArrayUtils.toObject(allIds)));
		assertTrue(idSet.contains(record0.getId()));
		assertTrue(idSet.contains(record1.getId()));
		assertTrue(idSet.contains(record2.getId()));
		assertTrue(idSet.contains(record3.getId()));
		assertTrue(idSet.contains(record4.getId()));
		assertTrue(idSet.contains(record5.getId()));
		assertTrue(idSet.contains(record6.getId()));
		
	}
}
