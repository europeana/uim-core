/* BlockingInitializerTest.java - created on May 31, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 31, 2011
 */
public class BlockingInitializerTest {
    /**
     * Tests a basic run through of a blocking initializer
     */
    @Test
    public void testBasicSetup() {
        BlockingInitializer blockingInitializer = new BlockingInitializer() {

            @Override
            protected void initializeInternal() {
                // do nothing
            }

        };
        assertEquals(blockingInitializer.getStatus(), BlockingInitializer.STATUS_NEW);
        blockingInitializer.initialize(ClassLoader.getSystemClassLoader());
        assertEquals(blockingInitializer.getStatus(), BlockingInitializer.STATUS_INITIALIZED);
    }

}
