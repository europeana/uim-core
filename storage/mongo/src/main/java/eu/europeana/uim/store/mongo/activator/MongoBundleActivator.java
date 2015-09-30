package eu.europeana.uim.store.mongo.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MongoBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {

	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static ClassLoader getBundleClassLoader() {
		// TODO Auto-generated method stub
		return MongoBundleActivator.class.getClassLoader();
	}

}
