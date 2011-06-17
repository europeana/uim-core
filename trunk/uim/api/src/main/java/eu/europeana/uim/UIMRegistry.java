package eu.europeana.uim;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;

/**
 * The central service registry for UIM. The service container registers all services with this
 * registry (as configured in the blueprint xml files) so that one can get an overview of registered
 * services for storage, logging, resources as well as workflows.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class UIMRegistry implements Registry {
    private static Logger                    log            = Logger.getLogger(UIMRegistry.class.getName());

    private String                           configuredStorageEngine;
    private StorageEngine<?>                 activeStorage  = null;
    private Map<String, StorageEngine<?>>    storages       = new HashMap<String, StorageEngine<?>>();

    private String                           configuredLoggingEngine;
    private LoggingEngine<?, ?>              activeLogging  = null;
    private Map<String, LoggingEngine<?, ?>> loggers        = new HashMap<String, LoggingEngine<?, ?>>();

    private String                           configuredResourceEngine;
    private ResourceEngine<?>                activeResource = null;
    private Map<String, ResourceEngine<?>>   resources      = new HashMap<String, ResourceEngine<?>>();

    private Map<String, IngestionPlugin>     plugins        = new HashMap<String, IngestionPlugin>();
    private Map<String, Workflow>            workflows      = new HashMap<String, Workflow>();

    private Orchestrator                     orchestrator   = null;

    /**
     * Creates a new instance of this class.
     */
    public UIMRegistry() {
        // nothing todo
    }

    @Override
    public void setConfiguredStorageEngine(String configuredStorageEngine) {
        // this may happen before the storage services are loaded
        // we set the active storage "lazy"
        this.configuredStorageEngine = configuredStorageEngine;
        if (this.activeStorage != null) {
            this.activeStorage = null;
            this.activeStorage = getStorageEngine(configuredStorageEngine);
        }
    }

    @Override
    public void setConfiguredLoggingEngine(String configuredLoggingEngine) {
        // this may happen before the storage services are loaded
        // we set the active logging "lazy"

        this.configuredLoggingEngine = configuredLoggingEngine;
        if (this.activeLogging != null) {
            this.activeLogging = null;
            this.activeLogging = getLoggingEngine(configuredLoggingEngine);
        }
    }

    @Override
    public void setConfiguredResourceEngine(String configuredResourceEngine) {
        // this may happen before the resource services are loaded
        // we set the active resource engine "lazy"

        this.configuredResourceEngine = configuredResourceEngine;
        if (this.activeResource != null) {
            this.activeResource = null;
            this.activeResource = getResourceEngine(configuredResourceEngine);
        }
    }

    @Override
    public List<Workflow> getWorkflows() {
        return new ArrayList<Workflow>(workflows.values());
    }

    @Override
    public Workflow getWorkflow(String identifier) {
        return workflows.get(identifier);
    }

    @Override
    public void addPlugin(IngestionPlugin plugin) {
        if (plugin != null) {
            checkPluginForNonStaticMemberVariables(plugin);

            log.info("Added plugin: " + plugin.getIdentifier());
            if (!plugins.containsKey(plugin.getIdentifier())) {
                plugin.initialize();
                plugins.put(plugin.getIdentifier(), plugin);
            }
        }
    }

    /**
     * Checks if the plugin contains non-static member variables
     * 
     * @param plugin
     */
    private void checkPluginForNonStaticMemberVariables(IngestionPlugin plugin) {
        log.info("Checking for non-static member-fields: " + plugin.getIdentifier());
        StringBuffer nonStaticMembers = new StringBuffer();

        // getFields() only accesses public fields - use getDeclaredFields() instead
        for (Field currentField : plugin.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(currentField.getModifiers())) continue;
            nonStaticMembers.append(currentField.getName() + " ");
        }
        if (nonStaticMembers.length() > 0)
            throw new IllegalArgumentException(plugin.getIdentifier() + " has non-static member(s): " +
                                               nonStaticMembers.toString());
    }

    @Override
    public IngestionPlugin getPlugin(String identifier) {
        return plugins.get(identifier);
    }

    @Override
    public void removePlugin(IngestionPlugin plugin) {
        if (plugin != null) {
            log.info("Removed plugin: " + plugin.getIdentifier());
            plugins.remove(plugin.getIdentifier());
            plugin.shutdown();
        }
    }

    @Override
    public void addStorageEngine(StorageEngine<?> storage) {
        if (storage != null) {
            log.info("Added storage: " + storage.getIdentifier());
            if (!storages.containsKey(storage.getIdentifier())) {
                storage.initialize();
                this.storages.put(storage.getIdentifier(), storage);

                // activate default storage
                if (storage.getIdentifier().equals(configuredStorageEngine)) {
                    activeStorage = storage;
                    log.info("Making storage " + storage.getIdentifier() + " default");
                }
            }
        }
    }

    @Override
    public void removeStorageEngine(StorageEngine<?> storage) {
        if (storage != null) {
            log.info("Removed storage: " + storage.getIdentifier());
            storage.shutdown();

            StorageEngine<?> remove = this.storages.remove(storage.getIdentifier());
            if (activeStorage == remove) {
                activeStorage = null;
            }
        }
    }

    @Override
    public Collection<StorageEngine<?>> getStorages() {
        return storages.values();
    }

    @Override
    public void addWorkflow(Workflow workflow) {
        if (workflow != null) {
            log.info("Added workflow: " + workflow.getName());
            if (!workflows.containsKey(workflow.getIdentifier()))
                workflows.put(workflow.getIdentifier(), workflow);
        }
    }

    @Override
    public void removeWorkflow(Workflow workflow) {
        if (workflow != null) {
            log.info("Removed workflow: " + workflow.getName());
            workflows.remove(workflow.getIdentifier());
        }
    }

    @Override
    public StorageEngine<?> getStorageEngine() {
        if (storages == null || storages.isEmpty()) return null;

        if (activeStorage == null) {
            if (getStorageEngine(configuredStorageEngine) != null) {
                activeStorage = getStorageEngine(configuredStorageEngine);
            } else {
                // default to first engine
                activeStorage = storages.values().iterator().next();

            }
        }
        return activeStorage;
    }

    StorageEngine<?> getActiveStorage() {
        return activeStorage;
    }

    @Override
    public StorageEngine<?> getStorageEngine(String identifier) {
        if (identifier == null || storages == null || storages.isEmpty()) return null;
        return storages.get(identifier);
    }

    @Override
    public void addLoggingEngine(LoggingEngine<?, ?> logging) {
        if (logging != null) {
            log.info("Added logging engine:" + logging.getIdentifier());
            if (!loggers.containsKey(logging.getIdentifier())) {
                loggers.put(logging.getIdentifier(), logging);
                // activate default logging
                if (activeLogging == null) {
                    activeLogging = logging;
                } else if (logging.getIdentifier().equals(configuredLoggingEngine)) {
                    activeLogging = logging;
                    log.info("Making logging engine " + logging.getIdentifier() + " default");
                }
            }
        }
    }

    @Override
    public void removeLoggingEngine(LoggingEngine<?, ?> logging) {
        if (logging != null) {

            LoggingEngine<?, ?> remove = loggers.remove(logging.getIdentifier());
            if (activeLogging == remove) {
                activeLogging = null;
            }

        }
    }

    @Override
    public List<LoggingEngine<?, ?>> getLoggingEngines() {
        List<LoggingEngine<?, ?>> res = new ArrayList<LoggingEngine<?, ?>>();
        res.addAll(loggers.values());
        return res;
    }

    @Override
    public LoggingEngine<?, ?> getLoggingEngine() {
        if (loggers == null || loggers.isEmpty()) return null;

        if (activeLogging == null) {
            if (getLoggingEngine(configuredLoggingEngine) != null) {
                activeLogging = getLoggingEngine(configuredLoggingEngine);
            } else {
                // default to first engine
                activeLogging = loggers.values().iterator().next();
            }
        }
        return activeLogging;
    }

    LoggingEngine<?, ?> getActiveLoggingEngine() {
        return activeLogging;
    }

    @Override
    public LoggingEngine<?, ?> getLoggingEngine(String identifier) {
        if (identifier == null || loggers == null || loggers.isEmpty()) return null;
        return loggers.get(identifier);
    }

    @Override
    public void addResourceEngine(ResourceEngine<?> resource) {
        if (resource != null) {
            log.info("Added resource engine:" + resource.getIdentifier());
            if (!resources.containsKey(resource.getIdentifier())) {
                resource.initialize();
                resources.put(resource.getIdentifier(), resource);

                // activate default resource engine
                if (activeResource == null) {
                    activeResource = resource;
                } else if (resource.getIdentifier().equals(configuredResourceEngine)) {
                    activeResource = resource;
                    log.info("Making resource engine " + resource.getIdentifier() + " default");
                }
            }
        }
    }

    @Override
    public void removeResourceEngine(ResourceEngine<?> resourceEngine) {
        if (resourceEngine != null) {
            ResourceEngine<?> remove = resources.remove(resourceEngine.getIdentifier());
            if (activeResource == remove) {
                activeResource = null;
            }

        }
    }

    @Override
    public List<ResourceEngine<?>> getResourceEngines() {
        List<ResourceEngine<?>> res = new ArrayList<ResourceEngine<?>>();
        res.addAll(resources.values());
        return res;
    }

    @Override
    public ResourceEngine<?> getResourceEngine() {
        if (resources == null || resources.isEmpty()) return null;

        if (activeResource == null) {
            if (getResourceEngine(configuredResourceEngine) != null) {
                activeResource = getResourceEngine(configuredResourceEngine);
            } else {
                // default to first engine
                activeResource = resources.values().iterator().next();
            }
        }
        return activeResource;
    }

    @Override
    public ResourceEngine<?> getResourceEngine(String identifier) {
        if (identifier == null || resources == null || resources.isEmpty()) return null;
        return resources.get(identifier);
    }

    ResourceEngine<?> getActiveResourceEngine() {
        return activeResource;
    }

    @Override
    public Orchestrator getOrchestrator() {
        return orchestrator;
    }

    @Override
    public void setOrchestrator(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Override
    public void unsetOrchestrator(Orchestrator orchestrator) {
        this.orchestrator = null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\nRegistered plugins:");
        builder.append("\n--------------------------------------");
        if (plugins.isEmpty()) {
            builder.append("\n\tNo plugins. ");
        } else {
            for (IngestionPlugin plugin : plugins.values()) {
                if (builder.length() > 0) {
                    builder.append("\n\tPlugin:");
                }
                builder.append(plugin.getIdentifier()).append("|").append(plugin.getName()).append(": [").append(plugin.getDescription()).append(
                        "]");
            }
        }

        builder.append("\nRegistered workflows:");
        builder.append("\n--------------------------------------");
        if (plugins.isEmpty()) {
            builder.append("\n\tNo workflows. ");
        } else {
            for (Workflow worfklow : workflows.values()) {
                if (builder.length() > 0) {
                    builder.append("\n\tWorkflow:");
                }
                builder.append(worfklow.getIdentifier()).append("|").append(worfklow.getName()).append(
                        ": [").append(worfklow.getDescription()).append("]");
            }
        }

        builder.append("\nRegistered storage:");
        builder.append("\n--------------------------------------");
        if (storages.isEmpty()) {
            builder.append("\n\tNo storage.");
        } else {
            for (StorageEngine<?> storage : storages.values()) {
                if (builder.length() > 0) {
                    builder.append("\n\t");
                }
                if (activeStorage != null && activeStorage == storage) {
                    builder.append("* ");
                } else {
                    builder.append("  ");
                }

                builder.append(storage.getIdentifier());
                builder.append(" [").append(storage.getStatus()).append("] ");
                builder.append(storage.getConfiguration().toString());
            }
        }
        
        builder.append("\nRegistered resource engines:");
        builder.append("\n--------------------------------------");
        if (resources.isEmpty()) {
            builder.append("\n\tNo resource engine.");
        } else {
            for (ResourceEngine<?> resourceEngine : resources.values()) {
                if (builder.length() > 0) {
                    builder.append("\n\t");
                }
                if (activeResource != null && activeResource == resourceEngine) {
                    builder.append("* ");
                } else {
                    builder.append("  ");
                }

                builder.append(resourceEngine.getIdentifier());
                builder.append(" [").append(resourceEngine.getStatus()).append("] ");
                builder.append(resourceEngine.getConfiguration().toString());
            }
        }


        builder.append("\nRegistered logging:");
        builder.append("\n--------------------------------------");
        if (loggers.isEmpty()) {
            builder.append("\n\tNo logging.");
        } else {
            for (LoggingEngine<?, ?> loggingEngine : loggers.values()) {
                if (builder.length() > 0) {
                    builder.append("\n\t");
                }
                if (activeLogging != null && activeLogging == loggingEngine) {
                    builder.append("* ");
                } else {
                    builder.append("  ");
                }
                builder.append(loggingEngine.getIdentifier());
            }
        }

        builder.append("\nRegistered orchestrator:");
        builder.append("\n--------------------------------------");
        builder.append(orchestrator != null ? "\n\t" + orchestrator.getIdentifier()
                : "\n\tNo orchestrator defined.");

        return builder.toString();
    }

}
