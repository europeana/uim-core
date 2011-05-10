package org.theeuropeanlibrary.uim.gui.gwt.server.engine;

import java.util.logging.Logger;

import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;

/**
 * This interface gives access to the elements of the UIM engine.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public abstract class Engine {

	private static Logger log = Logger.getLogger(Engine.class.getName());
	
	private static Engine instance;
	
	public static Engine getInstance() {
		if (instance == null) {
			// we are not started in an osgi container,
			// we need to create the Embedded engine.
			log.warning("No real/osgi engine set - creating a reflection based engine.");
			instance = new ReflectionEngine();
		}
		return instance;
	}
	
    protected static void setEngine(Engine newInstance) {
    	instance = newInstance;
    }

    
    public abstract Registry getRegistry();
    public abstract Orchestrator getOrchestrator();
    
    
}
