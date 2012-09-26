package eu.europeana.uim.gui.cp.server.engine;

import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.sugarcrm.SugarService;

/**
 * OSGI based implementation of engine.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public abstract class ExternalServiceEngine extends Engine {
    /**
     * @return repox service
     */
    public abstract RepoxService getRepoxService();

    /**
     * @return sugar service
     */
    public abstract SugarService getSugarService();
}