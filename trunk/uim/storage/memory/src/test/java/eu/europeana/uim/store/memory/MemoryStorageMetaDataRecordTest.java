package eu.europeana.uim.store.memory;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.AbstractMetaDataRecordTest;

/**
 * Metadata tests using in-memory implementation of storage.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
public class MemoryStorageMetaDataRecordTest extends AbstractMetaDataRecordTest<Long> {
	@Override
	protected StorageEngine<Long> getStorageEngine() {
		return new MemoryStorageEngine();
	}
}
