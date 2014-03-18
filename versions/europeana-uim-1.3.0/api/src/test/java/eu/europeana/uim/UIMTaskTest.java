/* UIMTaskTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.Test;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineAdapter;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;

/**
 * Tests task management (setup and workflow).
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class UIMTaskTest {
    /**
     * test method to test setter/getter pairs and default task setup.
     */
    @Test
    public void testTaskSetup() {
        StorageEngine<Long> engine = new StorageEngineAdapter<Long>() {
        };

        Task<Long> task = new Task<Long>(null, engine, null);

        assertNull(task.getStep());
        assertNull(task.getMetaDataRecord());
        assertNull(task.getOnFailure());
        assertNull(task.getOnSuccess());
        assertNull(task.getThrowable());

        assertEquals(TaskStatus.NEW, task.getStatus());

        task.setOnFailure(new LinkedList<Task<Long>>());
        task.setOnSuccess(new LinkedList<Task<Long>>());
        task.setThrowable(new Exception());

        assertNotNull(task.getOnFailure());
        assertNotNull(task.getOnSuccess());
        assertNotNull(task.getThrowable());
    }

    /**
     * test method to test simple workflow handling.
     */
    @Test
    public void testTaskWorkflow() {
        Task<Long> task = new Task<Long>(null, null, null);

        assertEquals(TaskStatus.NEW, task.getStatus());

        IngestionPlugin plugin = mock(IngestionPlugin.class);
        task.setStep(plugin, false);
        task.run();

        task.setOnFailure(new LinkedList<Task<Long>>());
        task.setOnSuccess(new LinkedList<Task<Long>>());
    }
}
