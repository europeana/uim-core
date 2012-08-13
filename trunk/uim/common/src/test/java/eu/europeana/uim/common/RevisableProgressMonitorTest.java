/* RevisableProgressMonitorTest.java - created on May 31, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for the revisable progress monitor
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 31, 2011
 */
public class RevisableProgressMonitorTest {
    /**
     * Test the revising and revisable progress monitor
     */
    @Test
    public void testRevisableProgressMonitor() {
        RevisableProgressMonitor revisableProgressMonitor = new RevisableProgressMonitor();
        RevisingProgressMonitor revisingProgressMonitor = new MemoryProgressMonitor();
        revisableProgressMonitor.addListener(revisingProgressMonitor);

        revisableProgressMonitor.beginTask("Task name", 100);
        for (int i = 0; i < 100; i++) {
            revisableProgressMonitor.worked(1);
        }
        assertEquals(100, revisableProgressMonitor.getWorked());
        assertEquals(100, revisingProgressMonitor.getWorked());
    }
}
