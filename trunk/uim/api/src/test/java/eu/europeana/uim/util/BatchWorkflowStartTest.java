/* BatchWorkflowStartTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.workflow.AbstractWorkflowStart;

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
		
		TKey<AbstractWorkflowStart,Serializable> key = TKey.resolve(AbstractWorkflowStart.class, "batches");

		when(execution.getDataSet()).thenReturn(collection);
		when(execution.getStorageEngine()).thenReturn(engine);
		when(engine.getByCollection((Collection)any())).thenReturn(new long[]{1,2,3,4,5,6,7,8,9,10,11});

		BatchWorkflowStart.BATCH_SIZE = 3;

		final BlockingQueue<long[]> batches = new LinkedBlockingQueue<long[]>();
		BatchWorkflowStart start = new BatchWorkflowStart(){
		    @Override
		    public BlockingQueue<long[]> getBatches(ExecutionContext context){
		        return batches;
		    }
		};
		
		start.initialize(execution, engine);
		try {
			start.createLoader(execution, engine).run();
			fail("Runnable is null when no data left.");
		} catch (Throwable t) {
		}		

		assertEquals(4, batches.size());
	}
}
