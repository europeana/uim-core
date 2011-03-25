package eu.europeana.uim.store.memory;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.AbstractStorageEngineTest;

/**
 * Storage tests using memory implementation of it.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
@RunWith(JUnit4.class)
public class MemoryStorageEngineTest extends AbstractStorageEngineTest<Long> {
    @Override
    protected StorageEngine<Long> getStorageEngine() {
        return new MemoryStorageEngine();
    }
}
