package eu.europeana.uim.gui.gwt.server.engine;

import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;

public class OsgiEngine extends Engine {

    private final Registry registry;
    private final Orchestrator orchestrator;

    public OsgiEngine(Registry registry, Orchestrator orchestrator) {
        this.registry = registry;
        this.orchestrator = orchestrator;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public Orchestrator getOrchestrator() {
        return orchestrator;
    }

    @Override
    public String toString() {
        return "UIM OSGI Engine";
    }
}