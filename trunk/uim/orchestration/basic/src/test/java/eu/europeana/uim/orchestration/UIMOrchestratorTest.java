package eu.europeana.uim.orchestration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.MDRFieldRegistry;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.memory.MemoryStorageEngine;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflows.MixedWorkflow;
import eu.europeana.uim.workflows.SyserrWorkflow;
import eu.europeana.uim.workflows.SysoutWorkflow;

/**
 * Tests UIM orchestration.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@SuppressWarnings("unchecked")
public class UIMOrchestratorTest {
    private Registry            registry;

    private StorageEngine<Long> engine;
    private UIMOrchestrator     orchestrator;

    /**
     * Sets up the orchistrator with a registry and a in memory storage engine.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        if (orchestrator == null) {
            registry = new UIMRegistry();
            engine = spy(new MemoryStorageEngine());

            registry.addStorageEngine(engine);
            registry.setConfiguredStorageEngine(MemoryStorageEngine.class.getSimpleName());

            UIMWorkflowProcessor processor = new UIMWorkflowProcessor(registry);
            orchestrator = new UIMOrchestrator(registry, processor);
        }
    }

    /**
     * Tests success of basic setup of orchestrator.o
     * 
     * @throws InterruptedException
     * @throws StorageEngineException
     */
    @Test
    public void testSimpleSuccessSetup() throws InterruptedException, StorageEngineException {
        assertEquals(0, orchestrator.getActiveExecutions().size());

        Request<Long> request = createTestData(engine, 1);

        // creating the data calles 20 times the update method.
        verify(engine, times(1)).updateMetaDataRecord(any(MetaDataRecord.class));

        Workflow w = new SysoutWorkflow(7, true, false);

        ActiveExecution<Long> execution0 = (ActiveExecution<Long>)orchestrator.executeWorkflow(w,
                request);
        execution0.waitUntilFinished();

        // each delivered metadata record is saved
        verify(engine, times(2)).updateMetaDataRecord(any(MetaDataRecord.class));

        assertEquals(1, execution0.getCompletedSize());
        assertEquals(0, execution0.getFailureSize());
        assertEquals(1, execution0.getScheduledSize());
    }

    /**
     * Tests failed setup.
     * 
     * @throws InterruptedException
     * @throws StorageEngineException
     */
    @Test
    public void testSimpleFailedSetup() throws InterruptedException, StorageEngineException {
        assertEquals(0, orchestrator.getActiveExecutions().size());

        Request<Long> request = createTestData((StorageEngine<Long>)registry.getStorage(), 21);
        // creating the data calles 21 times the update method.
        verify(engine, times(21)).updateMetaDataRecord(any(MetaDataRecord.class));

        Workflow w = new SyserrWorkflow(7, true);

        ActiveExecution<Long> execution0 = (ActiveExecution<Long>)orchestrator.executeWorkflow(w,
                request);
        execution0.waitUntilFinished();

        // each failed metadata record is saved once 21 + original count of 21
        verify(engine, times(42)).updateMetaDataRecord(any(MetaDataRecord.class));

        assertEquals(14, execution0.getCompletedSize());
        assertEquals(7, execution0.getFailureSize());
        assertEquals(21, execution0.getScheduledSize());
    }

    /**
     * Tests partial failures.
     * 
     * @throws InterruptedException
     * @throws StorageEngineException
     */
    @Test
    public void testSimplePartlyFailedSetup() throws InterruptedException, StorageEngineException {
        assertEquals(0, orchestrator.getActiveExecutions().size());

        Request<Long> request = createTestData((StorageEngine<Long>)registry.getStorage(), 30);
        // creating the data calles 30 times the update method.
        verify(engine, times(30)).updateMetaDataRecord(any(MetaDataRecord.class));

        Workflow w = new MixedWorkflow(7, true);

        ActiveExecution<Long> execution0 = (ActiveExecution<Long>)orchestrator.executeWorkflow(w,
                request);
        execution0.waitUntilFinished();

        // each failed metadata record is saved once 30 + original count of 30
        verify(engine, times(60)).updateMetaDataRecord(any(MetaDataRecord.class));

        assertEquals(20, execution0.getCompletedSize());
        assertEquals(10, execution0.getFailureSize());
        assertEquals(30, execution0.getScheduledSize());
    }
    /**
     * Tests partial failures.
     * 
     * @throws InterruptedException
     * @throws StorageEngineException
     */
    @Test
    public void testSimplePluginFailedSetup() throws InterruptedException, StorageEngineException {
        assertEquals(0, orchestrator.getActiveExecutions().size());

        Request<Long> request = createTestData((StorageEngine<Long>)registry.getStorage(), 30);
        // creating the data calles 30 times the update method.
        verify(engine, times(30)).updateMetaDataRecord(any(MetaDataRecord.class));

        Workflow w = new MixedWorkflow(7, true);

        Properties properties = new Properties();
        properties.setProperty("syserr.fullfailure", "true");
        
        ActiveExecution<Long> execution0 = (ActiveExecution<Long>)orchestrator.executeWorkflow(w,
                request, properties);
        execution0.waitUntilFinished();

        // each failed metadata record is saved once 30 + original count of 30
        verify(engine, times(32)).updateMetaDataRecord(any(MetaDataRecord.class));

        assertEquals(0, execution0.getCompletedSize());
        assertEquals(2, execution0.getFailureSize());
        assertEquals(30, execution0.getScheduledSize());
    }

    /**
     * Test save point.
     * 
     * @throws InterruptedException
     * @throws StorageEngineException
     */
    @Test
    public void testSavepointSuccess() throws InterruptedException, StorageEngineException {
        assertEquals(0, orchestrator.getActiveExecutions().size());

        Request<Long> request = createTestData(engine, 20);

        // creating the data calles 20 times the update method.
        verify(engine, times(20)).updateMetaDataRecord(any(MetaDataRecord.class));

        Workflow w = new SysoutWorkflow(7, true, true);

        ActiveExecution<Long> execution0 = (ActiveExecution<Long>)orchestrator.executeWorkflow(w,
                request);
        execution0.waitUntilFinished();

        // each delivered metadata record is saved once per plugin (only one plugin in the
        // workflow) 20 plus the initial 20
        verify(engine, times(40)).updateMetaDataRecord(any(MetaDataRecord.class));

        assertEquals(20, execution0.getCompletedSize());
        assertEquals(0, execution0.getFailureSize());
        assertEquals(20, execution0.getScheduledSize());
    }

    private Request<Long> createTestData(StorageEngine<Long> engine, int count)
            throws StorageEngineException {
        Provider<Long> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<Long> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<Long> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        for (int i = 0; i < count; i++) {
            MetaDataRecord<Long> record0 = engine.createMetaDataRecord(request0, "abcd" + i);
            record0.addField(MDRFieldRegistry.rawrecord, "title " + i);
            engine.updateMetaDataRecord(record0);
        }

        return request0;
    }
}
