package eu.europeana.uim.storage.memory;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.europeana.uim.storage.AbstractStorageEngineTest;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.memory.MemoryStorageEngine;

/**
 * Storage tests using memory implementation of it.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@RunWith(JUnit4.class)
public class MemoryStorageEngineTest extends AbstractStorageEngineTest<Long> {
    @Override
    protected StorageEngine<Long> getStorageEngine() {
        return new MemoryStorageEngine();
    }
}
