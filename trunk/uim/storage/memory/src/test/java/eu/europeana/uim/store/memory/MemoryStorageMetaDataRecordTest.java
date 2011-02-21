package eu.europeana.uim.store.memory;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.AbstractMetaDataRecordTest;

public class MemoryStorageMetaDataRecordTest extends AbstractMetaDataRecordTest {

	@Override
	protected StorageEngine getStorageEngine() {
		return new MemoryStorageEngine();
	}

}
