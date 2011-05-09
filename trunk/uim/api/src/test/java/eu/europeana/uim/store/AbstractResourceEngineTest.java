/* AbstractResourceEngineTest.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store;

import org.junit.Before;

import eu.europeana.uim.api.ResourceEngine;

/**
 * 
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I> 
 * @date May 9, 2011
 */
public abstract class AbstractResourceEngineTest<I> {
        ResourceEngine<I> engine = null;

      

        /**
         * Setups storage engine.
         */
        @Before
        public void setUp() {
            engine = getResourceEngine();
            performSetUp();
        }

        /**
         * Override this for additional setup
         */
        protected void performSetUp() {
            // nothing todo
        }

        /**
         * @return configured storage engine
         */
        protected abstract ResourceEngine<I> getResourceEngine();
        
        
}
