/* BatchWorkflowStartTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Properties;

import org.junit.Test;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineAdapter;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.util.BatchWorkflowStart.Data;

/**
 * Tests {@link BatchWorkflowStart} using mocks of {@link UimDataSet}s and
 * {@link StorageEngine}.
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
    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    @Test
    public void testInitialization() throws StorageEngineException {
        StorageEngine engine = mock(StorageEngineAdapter.class);

        Provider provider = mock(Provider.class);
        Collection collection = mock(Collection.class);
        Request request = mock(Request.class);

        Properties properties = new Properties();
        ActiveExecution execution = mock(ActiveExecution.class);

        Long[] batches = new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L};

        when(execution.getDataSet()).thenReturn(collection);
        when(execution.getStorageEngine()).thenReturn(engine);
        when(execution.getProperties()).thenReturn(properties);

        when(engine.getByCollection((Collection) any())).thenReturn(batches);
        when(engine.getByRequest((Request) any())).thenReturn(batches);

        Data data = new BatchWorkflowStart.Data();
        when(execution.getValue((TKey<?, Data>) any())).thenReturn(data);

        BatchWorkflowStart.BATCH_SIZE = 3;
        BatchWorkflowStart<Long> start = new BatchWorkflowStart<Long>();

        assertEquals(5, start.getPreferredThreadCount());
        assertEquals(10, start.getMaximumThreadCount());
        assertEquals(5, start.getParameters().size());

        start.initialize(execution);
        assertEquals(4, data.batches.size());

        data.batches.clear();

        when(execution.getDataSet()).thenReturn(request);
        start.initialize(execution);
        assertEquals(4, data.batches.size());

        data.batches.clear();

        MetaDataRecord record = new MetaDataRecordBean(1L, collection);
        when(execution.getDataSet()).thenReturn(record);
        start.initialize(execution);
        assertEquals(1, data.batches.size());

        data.batches.clear();

        when(execution.getDataSet()).thenReturn(collection);
        properties.setProperty(BatchWorkflowStart.BATCH_SUBSET_SHUFFLE, "5");
        start.initialize(execution);
        assertEquals(2, data.batches.size());

        TaskCreator loader = start.createLoader(execution);
        loader.setQueue(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
        loader.run();
    }
}
