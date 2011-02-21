/* BatchWorkflowStartTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.BlockingQueue;

import org.junit.Test;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class BatchWorkflowStartTest {


	@Test
	public void testInitialization() throws StorageEngineException {
		StorageEngine engine = mock(StorageEngine.class);
		Collection collection = mock(Collection.class);
		ActiveExecution execution = mock(ActiveExecution.class);

		when(execution.getDataSet()).thenReturn(collection);
		when(execution.getStorageEngine()).thenReturn(engine);
		when(engine.getByCollection((Collection)any())).thenReturn(new long[]{1,2,3,4,5,6,7,8,9,10,11});

		BatchWorkflowStart start = new BatchWorkflowStart(3);
		start.initialize(execution);
		start.createLoader(execution).run();
		start.createLoader(execution).run();
		start.createLoader(execution).run();
		start.createLoader(execution).run();
		try {
			start.createLoader(execution).run();
			fail("Runnable is null when no data left.");
		} catch (Throwable t) {
		}		

		BlockingQueue<long[]> batches = start.getBatches();
		assertEquals(4, batches.size());
	}
}
