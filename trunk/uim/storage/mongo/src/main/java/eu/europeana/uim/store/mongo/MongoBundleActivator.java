package eu.europeana.uim.store.mongo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MongoBundleActivator implements BundleActivator {

    private static ClassLoader classLoader;

    public static ClassLoader getBundleClassLoader() {
        if(classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        return classLoader;
    }

    public void start(BundleContext bundleContext) throws Exception {
        classLoader = this.getClass().getClassLoader();
    }

    public void stop(BundleContext bundleContext) throws Exception {
    }
}
