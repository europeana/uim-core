package eu.europeana.uim.api;

import java.util.Collection;
import java.util.List;

import eu.europeana.uim.workflow.Workflow;

/**
 * Registry for UIM services
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Registry {
    /**
     * @param plugin
     *            add plugin to registration
     */
    void addPlugin(IngestionPlugin plugin);

    /**
     * @param identifier
     * @return plugin registered under this identifier
     */
    IngestionPlugin getPlugin(String identifier);

    /**
     * @param plugin
     *            remove plugin to registration
     */
    void removePlugin(IngestionPlugin plugin);

    /**
     * @param storage
     *            add storage to registration
     */
    void addStorageEngine(StorageEngine<?> storage);

    /**
     * @param storage
     *            remove storage to registration
     */
    void removeStorageEngine(StorageEngine<?> storage);

    /**
     * @return known storages
     */
    Collection<StorageEngine<?>> getStorages();

    /**
     * @param configuredStorageEngine
     *            name of configured storage engine
     */
    void setConfiguredStorageEngine(String configuredStorageEngine);

    /**
     * @param configuredLoggingEngine
     *            name of configured logging engine
     */
    void setConfiguredLoggingEngine(String configuredLoggingEngine);

    /**
     * @param configuredResourceEngine
     *            name of the configured resource engine
     */
    void setConfiguredResourceEngine(String configuredResourceEngine);

    /**
     * @param workflow
     *            add workflow to registration
     */
    void addWorkflow(Workflow workflow);

    /**
     * @return registered workflows
     */
    List<Workflow> getWorkflows();

    /**
     * @param identifier unique identifier of workflow (most likely class name)
     * @return workflow for the given name or null
     */
    Workflow getWorkflow(String identifier);

    /**
     * @param workflow
     *            workflow plugin to registration
     */
    void removeWorkflow(Workflow workflow);

    /**
     * @return default storage
     */
    StorageEngine<?> getStorageEngine();

    /**
     * @param identifier
     * @return storage for identifier
     */
    StorageEngine<?> getStorageEngine(String identifier);

    /**
     * @param loggingEngine
     *            add logger to registration
     */
    void addLoggingEngine(LoggingEngine<?, ?> loggingEngine);

    /**
     * @param loggingEngine
     *            remove logger to registration
     */
    void removeLoggingEngine(LoggingEngine<?, ?> loggingEngine);

    /**
     * @return loggers
     */
    List<LoggingEngine<?, ?>> getLoggingEngines();

    /**
     * @return default logger
     */
    LoggingEngine<?, ?> getLoggingEngine();

    /**
     * @param identifier
     * @return logger for the given identifier or null
     */
    LoggingEngine<?, ?> getLoggingEngine(String identifier);

    /**
     * @param resourceEngine
     *            add logger to registration
     */
    void addResourceEngine(ResourceEngine<?> resourceEngine);

    /**
     * @param resourceEngine
     *            remove logger to registration
     */
    void removeResourceEngine(ResourceEngine<?> resourceEngine);

    /**
     * @return resourceEngines
     */
    List<ResourceEngine<?>> getResourceEngines();

    /**
     * @return default logger
     */
    ResourceEngine<?> getResourceEngine();

    /**
     * @param identifier
     * @return logger for the given identifier or null
     */
    ResourceEngine<?> getResourceEngine(String identifier);

    /**
     * @return registered orchestrator
     */
    Orchestrator getOrchestrator();

    /**
     * Registers the given orchestrator.
     * 
     * @param orchestrator
     */
    void setOrchestrator(Orchestrator orchestrator);

    /**
     * Unregisters the given orchestrator.
     * 
     * @param orchestrator
     */
    void unsetOrchestrator(Orchestrator orchestrator);
}
