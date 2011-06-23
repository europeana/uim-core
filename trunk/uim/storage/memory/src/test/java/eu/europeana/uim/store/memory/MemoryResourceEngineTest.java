/* MemoryResourceEngineTest.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.memory;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.store.AbstractResourceEngineTest;

/**
 * Implementation of test cases for memory based resource engine.
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 9, 2011
 */
@RunWith(JUnit4.class)
public class MemoryResourceEngineTest extends AbstractResourceEngineTest<Long> {
    @Override
    protected ResourceEngine getResourceEngine() {
        return new MemoryResourceEngine();
    }

    private static AtomicLong id = new AtomicLong();

    @Override
    protected Long nextID() {
        return id.incrementAndGet();
    }
}
