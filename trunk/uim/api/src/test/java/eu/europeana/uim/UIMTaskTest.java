/* UIMTaskTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineAdapter;
import eu.europeana.uim.workflow.AbstractWorkflowStep;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class UIMTaskTest {

	/** test method to test setter/getter pairs and default
	 * task setup.
	 * 
	 */
	@Test
	public void testTaskSetup() {
		StorageEngine engine = new StorageEngineAdapter() {};
		
		UIMTask task = new UIMTask(null, engine, null);

		assertNull(task.getStep());
		assertNull(task.getMetaDataRecord());
		assertNull(task.getOnFailure());
		assertNull(task.getOnSuccess());
		assertNull(task.getThrowable());

		assertEquals(TaskStatus.NEW, task.getStatus());

		task.setOnFailure(new LinkedList<Task>());
		task.setOnSuccess(new LinkedList<Task>());
		task.setThrowable(new Exception());
	
		assertNotNull(task.getOnFailure());
		assertNotNull(task.getOnSuccess());
		assertNotNull(task.getThrowable());
	}

	/** test method to test simple workflow handling.
	 */
	@Test
	public void testTaskWorkflow() {
		UIMTask task = new UIMTask(null, null, null);

		assertEquals(TaskStatus.NEW, task.getStatus());

		task.setStep(new AbstractWorkflowStep("test") {
			@Override
			public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
				throw new UnsupportedOperationException("Sorry, not implemented.");
			}

			@Override
			public int getPreferredThreadCount() {
				return 0;
			}

			@Override
			public int getMaximumThreadCount() {
				return 0;
			}
		});

		try {
			task.run();
			fail("There is an exception in the process method - task must fail");
		} catch (UnsupportedOperationException t) {
		}
	}
}
