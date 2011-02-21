/* ProgressMonitorTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;

import org.junit.Test;

import eu.europeana.uim.common.LoggingProgressMonitor;
import eu.europeana.uim.common.MemoryProgressMonitor;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class ProgressMonitorTest {

	/** test for the memory progress monitor
	 */
	@Test
	public void testMemoryProgressMonitor() {
		MemoryProgressMonitor monitor = new MemoryProgressMonitor();
		
		testProgressMonitor(monitor);
	}

	/** test for the logging progress monitor
	 */
	@Test
	public void testLoggingProgressMonitor() {
		MemoryProgressMonitor monitor = new LoggingProgressMonitor(Level.INFO, 50);
		testProgressMonitor(monitor);
	}

	
	private void testProgressMonitor(MemoryProgressMonitor monitor) {
		assertEquals(0, monitor.getWork());
		
		monitor.beginTask("test", 12);
		assertEquals(12, monitor.getWork());
		monitor.worked(3);
		assertEquals(3, monitor.getWorked());

		monitor.beginTask("test", 15);
		assertEquals(15, monitor.getWork());
		assertEquals(3, monitor.getWorked());

		monitor.subTask("subtask");
		assertEquals("subtask", monitor.getSubtask());
	}
}
