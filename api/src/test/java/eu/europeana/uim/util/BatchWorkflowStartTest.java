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
import eu.europeana.uim.util.CollectionBatchWorkflowStart.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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
        StorageEngine<Long> engine = mock(StorageEngineAdapter.class);

        Provider<Long> provider = mock(Provider.class);
        Collection<Long> collection = mock(Collection.class);
        Request<Long> request = mock(Request.class);

        Properties properties = new Properties();
        ActiveExecution execution = mock(ActiveExecution.class);

        List<Long> ids = new ArrayList<>();
        ids.add(1l);
        ids.add(2l);
        ids.add(3l);
        ids.add(4l);
        ids.add(5l);
        ids.add(6l);
        ids.add(7l);
        ids.add(8l);
        ids.add(9l);
        ids.add(10l);
        ids.add(11l);

        when(execution.getDataSet()).thenReturn(collection);
        when(execution.getStorageEngine()).thenReturn(engine);
        when(execution.getProperties()).thenReturn(properties);

        BatchWorkflowStart.Data data = new BatchWorkflowStart.Data();
        when(execution.getValue((TKey<?, Data>) any())).thenReturn(data);

        BatchWorkflowStart.DEFAULT_BATCH_SIZE = 3;
        BatchWorkflowStart<Long> start = new BatchWorkflowStart<>();

        assertEquals(5, start.getPreferredThreadCount());
        assertEquals(10, start.getMaximumThreadCount());
        assertEquals(5, start.getParameters().size());

        when(engine.getMetaDataRecordIdsByCollection((Collection) any())).thenReturn(new LinkedBlockingQueue<>(ids));
        start.initialize(execution);
        assertEquals(11, data.recordIds.size());

        data.recordIds.clear();

        when(execution.getDataSet()).thenReturn(request);
        when(engine.getMetaDataRecordIdsByRequest((Request) any())).thenReturn(new LinkedBlockingQueue<>(ids));
        start.initialize(execution);
        assertEquals(11, data.recordIds.size());

        data.recordIds.clear();

        MetaDataRecord record = new MetaDataRecordBean(1L, collection);
        when(execution.getDataSet()).thenReturn(record);
        start.initialize(execution);
        assertEquals(1, data.recordIds.size());

        data.recordIds.clear();
        
        when(engine.getMetaDataRecordIdsByCollection((Collection) any())).thenReturn(new LinkedBlockingQueue<>(ids));
        when(execution.getDataSet()).thenReturn(collection);
        properties.setProperty(BatchWorkflowStart.BATCH_SUBSET_SHUFFLE, "5");
        start.initialize(execution);
        assertEquals(5, data.recordIds.size());

        TaskCreator loader = start.createLoader(execution);
        loader.setQueue(new LinkedList<Task<MetaDataRecord<Long>, Long>>());
        loader.run();
    }
}
