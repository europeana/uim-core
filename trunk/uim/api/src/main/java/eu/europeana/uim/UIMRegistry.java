package eu.europeana.uim;

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
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;

/**
 * The central service registry for UIM. The service container registers all services with this
 * registry (as configured in the blueprint xml files) so that one can get an overview of registered
 * services for storage, logging as well as workflows.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class UIMRegistry implements Registry {
    private static Logger                 log           = Logger.getLogger(UIMRegistry.class.getName());

    private String                        configuredStorageEngine;
    private StorageEngine<?>              activeStorage = null;
    private Map<String, StorageEngine<?>> storages      = new HashMap<String, StorageEngine<?>>();

    private String                        configuredLoggingEngine;
    private LoggingEngine<?>              activeLogging = null;
    private Map<String, LoggingEngine<?>> loggers       = new HashMap<String, LoggingEngine<?>>();

    private Map<String, IngestionPlugin>  plugins       = new HashMap<String, IngestionPlugin>();
    private List<Workflow>                workflows     = new ArrayList<Workflow>();

    private Orchestrator                  orchestrator  = null;

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
            this.activeStorage = getStorage(configuredStorageEngine);
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
    public List<Workflow> getWorkflows() {
        return workflows;
    }

    @Override
    public Workflow getWorkflow(String identifier) {
        for (Workflow w : workflows) {
            if (w.getName().equals(identifier)) { return w; }
        }
        return null;
    }

    @Override
    public void addPlugin(IngestionPlugin plugin) {
        if (plugin != null) {
            log.info("Added plugin: " + plugin.getName());
            if (!plugins.containsKey(plugin.getName())) {
                plugin.initialize();
                plugins.put(plugin.getName(), plugin);
            }
        }
    }

    @Override
    public IngestionPlugin getPlugin(String identifier) {
        return plugins.get(identifier);
    }

    @Override
    public void removePlugin(IngestionPlugin plugin) {
        if (plugin != null) {
            log.info("Removed plugin: " + plugin.getName());
            plugins.remove(plugin.getName());
            plugin.shutdown();
        }
    }

    @Override
    public void addStorage(StorageEngine<?> storage) {
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
    public void removeStorage(StorageEngine<?> storage) {
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
            if (!workflows.contains(workflow)) workflows.add(workflow);
        }
    }

    @Override
    public void removeWorkflow(Workflow workflow) {
        if (workflow != null) {
            log.info("Removed workflow: " + workflow.getName());
            workflows.remove(workflow);
        }
    }

    @Override
    public StorageEngine<?> getStorage() {
        if (storages == null || storages.isEmpty()) return null;

        if (activeStorage == null) {
            if (getStorage(configuredStorageEngine) != null) {
                activeStorage = getStorage(configuredStorageEngine);
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
    public StorageEngine<?> getStorage(String identifier) {
        if (identifier == null || storages == null || storages.isEmpty()) return null;
        return storages.get(identifier);
    }

    @Override
    public void addLoggingEngine(LoggingEngine<?> logging) {
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
    public void removeLoggingEngine(LoggingEngine<?> logging) {
        if (logging != null) {

            LoggingEngine<?> remove = loggers.remove(logging.getIdentifier());
            if (activeLogging == remove) {
                activeLogging = null;
            }

        }
    }

    @Override
    public List<LoggingEngine<?>> getLoggingEngines() {
        List<LoggingEngine<?>> res = new ArrayList<LoggingEngine<?>>();
        res.addAll(loggers.values());
        return res;
    }

    @Override
    public LoggingEngine<?> getLoggingEngine() {
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

    LoggingEngine<?> getActiveLoggingEngine() {
        return activeLogging;
    }

    @Override
    public LoggingEngine<?> getLoggingEngine(String identifier) {
        if (identifier == null || loggers == null || loggers.isEmpty()) return null;
        return loggers.get(identifier);
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
                builder.append(plugin.getName()).append(": [").append(plugin.getDescription()).append(
                        "]");
            }
        }

        builder.append("\nRegistered workflows:");
        builder.append("\n--------------------------------------");
        if (plugins.isEmpty()) {
            builder.append("\n\tNo workflows. ");
        } else {
            for (Workflow worfklow : workflows) {
                if (builder.length() > 0) {
                    builder.append("\n\tWorkflow:");
                }
                builder.append(worfklow.getName()).append(": [").append(worfklow.getDescription()).append(
                        "]");
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

        builder.append("\nRegistered logging:");
        builder.append("\n--------------------------------------");
        if (loggers.isEmpty()) {
            builder.append("\n\tNo logging.");
        } else {
            for (LoggingEngine<?> loggingEngine : loggers.values()) {
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
