/* BatchWorkflowStartTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineAdapter;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.util.CollectionBatchWorkflowStart.Data;

/**
 * Tests {@link BatchWorkflowStart} using mocks of {@link UimDataSet}s and
 * {@link StorageEngine}.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class CollectionBatchWorkflowStartTest {

    /**
     * Tests initialization of {@link BatchWorkflowStart} with mocks.
     *
     * @throws StorageEngineException
     */
    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    @Test
    public void testInitialization() throws StorageEngineException {
        StorageEngine engine = mock(StorageEngineAdapter.class);

        Provider provider = mock(Provider.class);
        Collection collection = mock(Collection.class);
        Request request = mock(Request.class);

        Properties properties = new Properties();
        ActiveExecution execution = mock(ActiveExecution.class);

        when(execution.getDataSet()).thenReturn(collection);
        when(execution.getStorageEngine()).thenReturn(engine);
        when(execution.getProperties()).thenReturn(properties);

        Data data = new CollectionBatchWorkflowStart.Data();
        when(execution.getValue((TKey<?, Data>) any())).thenReturn(data);

        CollectionBatchWorkflowStart<Long> start = new CollectionBatchWorkflowStart<Long>();

        assertEquals(2, start.getPreferredThreadCount());
        assertEquals(4, start.getMaximumThreadCount());
        assertEquals(0, start.getParameters().size());

        start.initialize(execution);
        Assert.assertNotNull(data.collection);

        TaskCreator loader = start.createLoader(execution);
        loader.setQueue(new LinkedList<Task<Collection<Long>, Long>>());
        loader.run();
    }
}
