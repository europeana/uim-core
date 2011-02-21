package eu.europeana.uim.gui.gwt.server;

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
public class UIMDependenciesActivator implements BundleActivator {

    private static OSGIUIMEngine engine = null;

    public static UIMEngine getUIMEngine() {
        if(engine == null) {
            throw new RuntimeException("UIM Engine not ready");
        }
        return engine;
    }

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

        engine = new OSGIUIMEngine(registry, orchestrator);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        engine = null;
    }

    static class OSGIUIMEngine implements UIMEngine {

        private final Registry registry;
        private final Orchestrator orchestrator;

        OSGIUIMEngine(Registry registry, Orchestrator orchestrator) {
            this.registry = registry;
            this.orchestrator = orchestrator;
        }

        @Override
        public Registry getRegistry() {
            return registry;
        }

        @Override
        public Orchestrator getOrchestrator() {
            return orchestrator;
        }

        @Override
        public String toString() {
            return "UIM OSGI Engine";
        }
    }


}

