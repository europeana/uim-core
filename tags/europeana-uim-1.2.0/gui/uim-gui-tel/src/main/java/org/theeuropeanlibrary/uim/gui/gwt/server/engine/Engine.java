package org.theeuropeanlibrary.uim.gui.gwt.server.engine;

import java.util.logging.Logger;

import eu.europeana.uim.api.Registry;

/**
 * This interface gives access to the elements of the UIM engine.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
public abstract class Engine {
    private static Logger log = Logger.getLogger(Engine.class.getName());

    private static Engine instance;

    /**
     * @return singleton instance of engine.
     */
    public static Engine getInstance() {
        if (instance == null) {
            // we are not started in an osgi container,
            // we need to create the Embedded engine.
            log.warning("No real/osgi engine set - creating a reflection based engine.");
            instance = new ReflectionEngine();
        }
        return instance;
    }

    /**
     * @param newInstance
     *            sets an engine instance to be used througout the application
     */
    protected static void setEngine(Engine newInstance) {
        instance = newInstance;
    }

    /**
     * @return registry
     */
    public abstract Registry getRegistry();
}
