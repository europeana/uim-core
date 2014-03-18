package eu.europeana.uim.gui.cp.server.engine;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repox.RepoxService;

/**
 * OSGI based implementation of engine.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public class RepoxOsgiEngine extends RepoxEngine {
    private final Registry     registry;
    private final RepoxService repoxService;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param repoxService
     */
    public RepoxOsgiEngine(Registry registry, RepoxService repoxService) {
        this.registry = registry;
        this.repoxService = repoxService;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public RepoxService getRepoxService() {
        return repoxService;
    }

    @Override
    public String toString() {
        return "UIM OSGI Engine with Repox Service";
    }
}