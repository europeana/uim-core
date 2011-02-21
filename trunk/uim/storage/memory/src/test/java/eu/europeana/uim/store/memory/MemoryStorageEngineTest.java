package eu.europeana.uim.store.memory;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.AbstractStorageEngineTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MemoryStorageEngineTest extends AbstractStorageEngineTest {

	@Override
	protected StorageEngine getStorageEngine() {
		return new MemoryStorageEngine();
	}

}
