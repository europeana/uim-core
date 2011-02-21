package eu.europeana.uim.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;

public abstract class AbstractMetaDataRecordTest {

	StorageEngine engine = getStorageEngine();

	protected abstract StorageEngine getStorageEngine();

	@Test
	public void testNullNotNullFields() throws StorageEngineException {
		Request request = createRequest();
		
		MetaDataRecord record = engine.createMetaDataRecord(request);

		// no value for this field exists, therefore the first field is null
		assertNull(record.getFirstField(MDRFieldRegistry.rawformat));
		
		// never return null - get empty list when nothing is there
		assertNotNull(record.getField(MDRFieldRegistry.rawformat));
		// never return null - get empty list when nothing is there
		assertNotNull(record.getQField(MDRFieldRegistry.rawformat, "EN"));
	}
	
	
	protected Request createRequest() throws StorageEngineException {
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

		return request0;
	}
}
