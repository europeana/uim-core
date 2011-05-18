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

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public OsgiEngine(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public String toString() {
        return "UIM OSGI Engine";
    }
}