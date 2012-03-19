package eu.europeana.uim.gui.cp.server.engine;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import eu.europeana.uim.api.Registry;

/**
 * This bundle activator serves as a dependency provisioning mechanism to the GWT RemoteServices. We
 * need this mechanism since our servlets aren't being managed by the OSGI framework but by the
 * servlet container
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 17, 2011
 */
public class OsgiEngineActivator implements BundleActivator {
    private static Engine engine = null;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Registry registry = null;

        int wait = 0;
        while (registry == null && wait++ < 10) {
            ServiceReference registryRef = bundleContext.getServiceReference("eu.europeana.uim.api.Registry");
            if (registryRef != null) {
                registry = (Registry)bundleContext.getService(registryRef);
            }
            Thread.sleep(1000);
        }

//        RepoxService repoxService = null;
//
//        wait = 0;
//        while (repoxService == null && wait++ < 10) {
//            ServiceReference repoxServiceRef = bundleContext.getServiceReference("eu.europeana.uim.repox.RepoxService");
//            if (repoxServiceRef != null) {
//                repoxService = (RepoxService)bundleContext.getService(repoxServiceRef);
//            }
//            Thread.sleep(1000);
//        }
//
//        if (repoxService != null) {
//            engine = new RepoxOsgiEngine(registry, repoxService);
//        } else {
            engine = new OsgiEngine(registry);
//        }
        Engine.setEngine(engine);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        engine = null;
    }
}
