package eu.europeana.uim.orchestration;


import java.util.Date;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.MemoryProgressMonitor;
import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.memory.MemoryStorageEngine;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflows.MixedWorkflow;
import eu.europeana.uim.workflows.SyserrWorkflow;
import eu.europeana.uim.workflows.SysoutWorkflow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UIMOrchestratorTest {

	private Registry registry;
	
	private StorageEngine engine;
	private UIMOrchestrator orchestrator;
	
	@Before
	public void setUp() throws Exception {
		if (orchestrator == null) {
			registry = new UIMRegistry();
			engine = spy(new MemoryStorageEngine());
			
			registry.addStorage(engine);
			registry.setConfiguredStorageEngine(MemoryStorageEngine.class.getSimpleName());

            UIMWorkflowProcessor processor = new UIMWorkflowProcessor(registry);
			orchestrator = new UIMOrchestrator(registry, processor);
		}
	}

	
	@Test
	public void testSimpleSuccessSetup() throws InterruptedException, StorageEngineException {
		assertEquals(0, orchestrator.getActiveExecutions().size());

		
		Request request = createTestData(engine, 20);
		
		// creating the data calles 20 times the update method.
		verify(engine, times(20)).updateMetaDataRecord(any(MetaDataRecord.class));

		Workflow w = new SysoutWorkflow(3, 7, true, false);
        ProgressMonitor monitor = new MemoryProgressMonitor();

		
		ActiveExecution<Task> execution0 = orchestrator.executeWorkflow(w, request, monitor);
		execution0.waitUntilFinished();

		// each delivered metadata record is saved
		verify(engine, times(40)).updateMetaDataRecord(any(MetaDataRecord.class));

		assertEquals(20, execution0.getCompletedSize());
		assertEquals(0, execution0.getFailureSize());
		assertEquals(20, execution0.getScheduledSize());
	}
	
	
	@Test
	public void testSimpleFailedSetup() throws InterruptedException, StorageEngineException {
		assertEquals(0, orchestrator.getActiveExecutions().size());

		Request request = createTestData(registry.getStorage(), 21);
		// creating the data calles 21 times the update method.
		verify(engine, times(21)).updateMetaDataRecord(any(MetaDataRecord.class));


		Workflow w = new SyserrWorkflow(7, true);
        ProgressMonitor monitor = new MemoryProgressMonitor();

		
		ActiveExecution<Task> execution0 = orchestrator.executeWorkflow(w, request, monitor);
		execution0.waitUntilFinished();
		
		// each failed metadata record is saved once 21 + original count of 21
		verify(engine, times(42)).updateMetaDataRecord(any(MetaDataRecord.class));

		assertEquals(0, execution0.getCompletedSize());
		assertEquals(21, execution0.getFailureSize());
		assertEquals(21, execution0.getScheduledSize());
	}
	
	
	@Test
	public void testSimplePartlyFailedSetup() throws InterruptedException, StorageEngineException {
		assertEquals(0, orchestrator.getActiveExecutions().size());

		Request request = createTestData(registry.getStorage(), 30);
		// creating the data calles 30 times the update method.
		verify(engine, times(30)).updateMetaDataRecord(any(MetaDataRecord.class));


		Workflow w = new MixedWorkflow(7, true, true);
        ProgressMonitor monitor = new MemoryProgressMonitor();

		
		ActiveExecution<Task> execution0 = orchestrator.executeWorkflow(w, request, monitor);
		execution0.waitUntilFinished();
		
		// each failed metadata record is saved once 30 + original count of 30
		verify(engine, times(60)).updateMetaDataRecord(any(MetaDataRecord.class));


		
		assertEquals(15, execution0.getCompletedSize());
		assertEquals(15, execution0.getFailureSize());
		assertEquals(30, execution0.getScheduledSize());
	}
	
	
	@Test
	public void testSavepointSuccess() throws InterruptedException, StorageEngineException {
		assertEquals(0, orchestrator.getActiveExecutions().size());

		
		Request request = createTestData(engine, 20);
		
		// creating the data calles 20 times the update method.
		verify(engine, times(20)).updateMetaDataRecord(any(MetaDataRecord.class));

		Workflow w = new SysoutWorkflow(3, 7, true, true);
        ProgressMonitor monitor = new MemoryProgressMonitor();

		
		ActiveExecution<Task> execution0 = orchestrator.executeWorkflow(w, request, monitor);
		execution0.waitUntilFinished();

		// each delivered metadata record is saved once per plugin plus additionally
		// once in the end 3 * 20 + 20 = 80 plus the initial 20 
		verify(engine, times(100)).updateMetaDataRecord(any(MetaDataRecord.class));

		assertEquals(20, execution0.getCompletedSize());
		assertEquals(0, execution0.getFailureSize());
		assertEquals(20, execution0.getScheduledSize());
	}
	
	

	
	private Request createTestData(StorageEngine engine, int count) throws StorageEngineException {
		Provider provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);
		
		Collection collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);
		
		Request request0 = engine.createRequest(collection0, new Date(0));
		engine.updateRequest(request0);
		
		for(int i = 0 ; i < count; i++) {
			MetaDataRecord record0 = engine.createMetaDataRecord(request0, "abcd" + i);
			record0.addField(MDRFieldRegistry.title, "title " + i);
			engine.updateMetaDataRecord(record0);
		}

		return request0;
	}
	
	
	
}
