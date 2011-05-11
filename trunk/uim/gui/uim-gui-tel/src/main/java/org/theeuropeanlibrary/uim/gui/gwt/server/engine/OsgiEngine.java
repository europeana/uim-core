package org.theeuropeanlibrary.uim.gui.gwt.server.engine;

import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;

/**
 * OSGI based implementation of engine.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public class OsgiEngine extends Engine {
    private final Registry     registry;
    private final Orchestrator orchestrator;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param orchestrator
     */
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