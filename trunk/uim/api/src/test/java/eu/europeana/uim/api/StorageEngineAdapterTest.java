/* StorageEngineAdapterTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineAdapter;
import eu.europeana.uim.api.StorageEngineException;

/**
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class StorageEngineAdapterTest {

	/**
	 * @throws StorageEngineException
	 */
	@Test
	public void testStorageAdapterNoop() throws StorageEngineException {
		StorageEngine engine = new StorageEngineAdapter() {	};
		
		assertNull(engine.createProvider());
		assertNull(engine.createCollection(null));
		assertNull(engine.createRequest(null, null));
		assertNull(engine.createMetaDataRecord(null));
		assertNull(engine.createMetaDataRecord(null, null));
		
		engine.updateProvider(null);
		engine.updateCollection(null);
		engine.updateRequest(null);
		engine.updateMetaDataRecord(null);
		
		assertNotNull(engine.getAllProviders());
		assertNotNull(engine.getAllCollections());
		
		assertNotNull(engine.getAllIds());
		assertNotNull(engine.getByRequest(null));
		assertNotNull(engine.getByCollection(null));
		assertNotNull(engine.getByProvider(null, false));
		assertNotNull(engine.getByProvider(null, true));
		
		assertEquals(0,engine.getTotalByRequest(null));
		assertEquals(0,engine.getTotalByCollection(null));
		assertEquals(0,engine.getTotalByProvider(null, false));
		assertEquals(0,engine.getTotalByProvider(null, true));
	}
}
