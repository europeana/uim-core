package eu.europeana.uim.gui.cp.server.engine;

import eu.europeana.uim.Registry;
import eu.europeana.uim.external.ExternalService;

/**
 * OSGI based implementation of engine.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public class ExternalServiceOsgiEngine extends ExternalServiceEngine {
    private final Registry     registry;
    private final ExternalService repoxService;
    private final ExternalService sugarService;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param sugarService
     * @param repoxService
     */
    public ExternalServiceOsgiEngine(Registry registry, ExternalService sugarService,
            ExternalService repoxService) {
        this.registry = registry;
        this.sugarService = sugarService;
        this.repoxService = repoxService;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public ExternalService getSugarService() {
        return sugarService;
    }

    @Override
    public ExternalService getRepoxService() {
        return repoxService;
    }

    @Override
    public String toString() {
        return "UIM OSGI Engine with Repox service and Sugar CRM service";
    }
}