package org.theeuropeanlibrary.uim.gui.gwt.server.engine;

import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This bundle activator serves as a dependency provisioning mechanism to the GWT RemoteServices. We need this mechanism
 * since our servlets aren't being managed by the OSGI framework but by the servlet container
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class OsgiEngineActivator implements BundleActivator {

    private static OsgiEngine engine = null;


    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Registry registry = null;
        Orchestrator orchestrator = null;
        ServiceReference registryRef = bundleContext.getServiceReference("eu.europeana.uim.api.Registry");
        if(registryRef != null) {
            registry = (UIMRegistry) bundleContext.getService(registryRef);
        }
        ServiceReference orchestratorRef = bundleContext.getServiceReference("eu.europeana.uim.api.Orchestrator");
        if(orchestratorRef != null) {
            orchestrator = (Orchestrator) bundleContext.getService(orchestratorRef);
        }

        engine = new OsgiEngine(registry, orchestrator);
        Engine.setEngine(engine);
    }
    

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        engine = null;
    }


}

