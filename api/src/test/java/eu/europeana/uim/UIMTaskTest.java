/* UIMTaskTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.Test;

import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskStatus;
import eu.europeana.uim.store.MetaDataRecord;

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
        Task<MetaDataRecord<Long>, Long> task = new Task<MetaDataRecord<Long>, Long>(null, null);

        assertNull(task.getStep());
        assertNull(task.getDataset());
        assertNull(task.getOnFailure());
        assertNull(task.getOnSuccess());
        assertNull(task.getThrowable());

        assertEquals(TaskStatus.NEW, task.getStatus());

        task.setOnFailure(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
        task.setOnSuccess(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
        task.setThrowable(new Exception());

        assertNotNull(task.getOnFailure());
        assertNotNull(task.getOnSuccess());
        assertNotNull(task.getThrowable());
    }

    /**
     * test method to test simple workflow handling.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTaskWorkflow() {
        Task<MetaDataRecord<Long>, Long> task = new Task<MetaDataRecord<Long>, Long>(null, null);

        assertEquals(TaskStatus.NEW, task.getStatus());

        IngestionPlugin<MetaDataRecord<Long>, Long> plugin = mock(IngestionPlugin.class);
        task.setStep(plugin, false);
        task.run();

        task.setOnFailure(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
        task.setOnSuccess(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
    }
}
