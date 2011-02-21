package eu.europeana.uim.gui.gwt.server;

import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;

/**
 * This interface gives access to the elements of the UIM engine.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface UIMEngine {

    Registry getRegistry();
    Orchestrator getOrchestrator();
}
