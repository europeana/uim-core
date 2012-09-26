/* CallbackProgressMonitorTest.java - created on May 31, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import org.junit.Test;

/**
 * Tests for the CallbackProgressMonitor
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 31, 2011
 */
public class CallbackProgressMonitorTest {

    /**
     * Test a basic runthrough
     */
    @Test
    public void testCallbackProgressMonitor() {

        CallbackProgressMonitor callbackProgressMonitor = new CallbackProgressMonitor(10) {

            @Override
            public void event(int worked) {
                if (worked % 10 != 0) { throw new IllegalStateException(
                        "Did not arrived here at the right place"); }

            }
        };
        callbackProgressMonitor.beginTask("Task name", 100);
        for (int i = 0; i < 200; i++) {
            callbackProgressMonitor.worked(1);
        }
        
    }
}
