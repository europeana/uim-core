package eu.europeana.uim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.logging.LoggingEngineAdapter;
import eu.europeana.uim.orchestration.Orchestrator;
import eu.europeana.uim.plugin.Plugin;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.resource.ResourceEngineAdapter;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineAdapter;
import eu.europeana.uim.workflow.Workflow;

/**
 * Tests registration of storage, logging and resource engine.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class UIMRegistryTest {
    private UIMRegistry registry = new UIMRegistry();

    /**
     * Tests registration of a {@link StorageEngine}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testRegistryOutput() {
        registry.setConfiguredStorageEngine(StorageEngineAdapter.class.getSimpleName());
        StorageEngineAdapter storage = new StorageEngineAdapter() {
        };
        registry.addStorageEngine(storage);
        LoggingEngineAdapter logging = new LoggingEngineAdapter() {
        };
        registry.addLoggingEngine(logging);
        ResourceEngineAdapter resource = new ResourceEngineAdapter() {
        };
        registry.addResourceEngine(resource);
        LegalIngestionPlugin<Long> plugin = new LegalIngestionPlugin<Long>();
        registry.addPlugin(plugin);
        LegalIngestionWorkflow<Long> workflow = new LegalIngestionWorkflow<Long>();
        registry.addWorkflow(workflow);
        Orchestrator orchestrator = mock(Orchestrator.class);
        registry.setOrchestrator(orchestrator);

        assertEquals(663, registry.toString().length());
    }

    /**
     * Tests registration of a {@link StorageEngine}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testStorageEngine() {
        registry.setConfiguredStorageEngine(StorageEngineAdapter.class.getSimpleName());
        StorageEngineAdapter storage = new StorageEngineAdapter() {
        };
        registry.addStorageEngine(storage);

        assertNotNull(registry.getStorageEngine());
        registry.setConfiguredStorageEngine("a");
        assertNull(registry.getActiveStorageEngine());
        assertNotNull(registry.getStorageEngine());
        assertNotNull(registry.getStorageEngine(StorageEngineAdapter.class.getSimpleName()));
        assertEquals(1, registry.getStorageEngines().size());

        registry.removeStorageEngine(storage);
    }

    /**
     * Tests registration of a {@link LoggingEngine}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testLoggingEngine() {
        registry.setConfiguredLoggingEngine(LoggingEngineAdapter.class.getSimpleName());
        LoggingEngineAdapter logging = new LoggingEngineAdapter() {
        };
        registry.addLoggingEngine(logging);
        assertNotNull(registry.getLoggingEngine());

        registry.setConfiguredLoggingEngine("a");
        assertNull(registry.getActiveLoggingEngine());
        assertNotNull(registry.getLoggingEngine());
        assertNotNull(registry.getLoggingEngine(LoggingEngineAdapter.class.getSimpleName()));

        assertEquals(1, registry.getLoggingEngines().size());
        registry.removeLoggingEngine(logging);
    }

    /**
     * Tests registration of a {@link ResourceEngine}.
     */
    @Test
    public void testResourceEngine() {
        registry.setConfiguredLoggingEngine(ResourceEngineAdapter.class.getSimpleName());
        ResourceEngineAdapter resource = new ResourceEngineAdapter() {
        };
        registry.addResourceEngine(resource);
        assertNotNull(registry.getResourceEngine());

        registry.setConfiguredResourceEngine("a");
        assertNull(registry.getActiveResourceEngine());
        assertNotNull(registry.getResourceEngine());
        assertNotNull(registry.getResourceEngine(ResourceEngineAdapter.class.getSimpleName()));

        assertEquals(1, registry.getResourceEngines().size());

        registry.removeResourceEngine(resource);
    }

    /**
     * Test if registration fails registration
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPluginMemberFieldCheckToFail() {
        registry.addPlugin(new IllegalIngestionPlugin<Long>());
    }

    /**
     * Test if registration fails registration
     */
    @Test
    public void testPluginMemberFieldCheckToSucceed() {
        registry.addPlugin(new LegalIngestionPlugin<Long>());
    }

    /**
     * Test if registration fails registration
     */
    @Test
    public void testPluginRoundTrip() {
        assertTrue(registry.getPlugins().isEmpty());

        LegalIngestionPlugin<Long> plugin = new LegalIngestionPlugin<Long>();
        registry.addPlugin(plugin);
        assertFalse(registry.getPlugins().isEmpty());

        Plugin plugin2 = registry.getPlugin(plugin.getIdentifier());
        assertSame(plugin, plugin2);

        plugin2 = registry.getPlugin(new LegalIngestionPlugin<Long>().getIdentifier());
        assertSame(plugin, plugin2);
    }

    /**
     * Test if registration fails registration
     */
    @Test
    public void testWorkflowRoundTrip() {
        assertTrue(registry.getWorkflows().isEmpty());

        LegalIngestionWorkflow<Long> plugin = new LegalIngestionWorkflow<Long>();
        registry.addWorkflow(plugin);
        assertFalse(registry.getWorkflows().isEmpty());

        Workflow<?, ?> plugin2 = registry.getWorkflow(plugin.getIdentifier());
        assertSame(plugin, plugin2);

        plugin2 = registry.getWorkflow(new LegalIngestionWorkflow<Long>().getIdentifier());
        assertSame(plugin, plugin2);
    }

    /**
     * Test getter setter tuples
     */
    @Test
    public void testGetterSetterCheckToSucceed() {
        assertNull(registry.getOrchestrator());

        Orchestrator<?> orchestrator = mock(Orchestrator.class);
        registry.setOrchestrator(orchestrator);
        assertNotNull(registry.getOrchestrator());

        registry.unsetOrchestrator(orchestrator);
        assertNull(registry.getOrchestrator());
    }
}
