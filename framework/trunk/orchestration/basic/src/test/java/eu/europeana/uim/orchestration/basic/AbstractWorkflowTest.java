package eu.europeana.uim.orchestration.basic;

import static org.mockito.Mockito.spy;

import org.junit.Before;

import eu.europeana.uim.Registry;
import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.memory.MemoryResourceEngine;
import eu.europeana.uim.storage.memory.MemoryStorageEngine;

/**
 * Abstract class for workflow tests. Setting up UIM framework and provides callback method to fill
 * records.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public abstract class AbstractWorkflowTest {
    /**
     * registry
     */
    protected Registry              registry;
    /**
     * storage
     */
    protected StorageEngine<Long>   engine;
    /**
     * resource
     */
    protected ResourceEngine        resource;
    /**
     * orchestrator
     */
    protected UIMOrchestrator<Long> orchestrator;

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

            resource = new MemoryResourceEngine();
            registry.addResourceEngine(resource);
            registry.setConfiguredResourceEngine(MemoryResourceEngine.class.getSimpleName());

            UIMWorkflowProcessor<Long> processor = new UIMWorkflowProcessor<Long>(registry);
            orchestrator = new UIMOrchestrator<Long>(registry, processor);
        }
    }
}
