/* BatchWorkflowStartTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Test;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineAdapter;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.util.BatchWorkflowStart.Data;

/**
 * Tests {@link BatchWorkflowStart} using mocks of {@link UimDataSet}s and {@link StorageEngine}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class BatchWorkflowStartTest {
    /**
     * Tests initialization of {@link BatchWorkflowStart} with mocks.
     * 
     * @throws StorageEngineException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testInitialization() throws StorageEngineException {
        StorageEngine engine = mock(StorageEngineAdapter.class);
        Collection collection = mock(Collection.class);
        ActiveExecution execution = mock(ActiveExecution.class);

        Long[] batches = new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L };

        when(execution.getDataSet()).thenReturn(collection);
        when(execution.getStorageEngine()).thenReturn(engine);
        when(execution.getProperties()).thenReturn(new Properties());
        when(engine.getByCollection((Collection)any())).thenReturn(batches);

        Data data = new BatchWorkflowStart.Data();
        when(execution.getValue((TKey<?, Data>)any())).thenReturn(data);

        BatchWorkflowStart.BATCH_SIZE = 3;
        BatchWorkflowStart start = new BatchWorkflowStart();

        start.initialize(execution, engine);
        assertEquals(4, data.batches.size());
    }
}
