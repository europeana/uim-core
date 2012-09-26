package eu.europeana.uim.gui.cp.server.engine;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.sugarcrm.SugarService;

/**
 * OSGI based implementation of engine.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public class ExternalServiceOsgiEngine extends ExternalServiceEngine {
    private final Registry     registry;
    private final RepoxService repoxService;
    private final SugarService sugarService;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param sugarService
     * @param repoxService
     */
    public ExternalServiceOsgiEngine(Registry registry, SugarService sugarService,
                                     RepoxService repoxService) {
        this.registry = registry;
        this.sugarService = sugarService;
        this.repoxService = repoxService;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public SugarService getSugarService() {
        return sugarService;
    }

    @Override
    public RepoxService getRepoxService() {
        return repoxService;
    }

    @Override
    public String toString() {
        return "UIM OSGI Engine with Repox service and Sugar CRM service";
    }
}