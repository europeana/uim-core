package eu.europeana.uim.gui.cp.server.engine;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.Registry;

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
        ServiceReference registryRef = bundleContext.getServiceReference("eu.europeana.uim.api.Registry");
        if(registryRef != null) {
            registry = (UIMRegistry) bundleContext.getService(registryRef);
        }

        engine = new OsgiEngine(registry);
        Engine.setEngine(engine);
    }
    

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        engine = null;
    }
}

