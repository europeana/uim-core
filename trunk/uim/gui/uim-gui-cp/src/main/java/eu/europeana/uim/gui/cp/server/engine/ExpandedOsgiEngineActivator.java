/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.gui.cp.server.engine;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.sugarcrmclient.plugin.SugarCRMService;

/**
 * @author georgiosmarkakis
 *
 */
public class ExpandedOsgiEngineActivator implements BundleActivator {

    private static ExpandedOsgiEngine expengine = null;

    
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
        Registry registry = null;
        RepoxUIMService repoxService = null;
        SugarCRMService sugarCrmService = null;

        int wait = 0;
        while (registry == null && wait++ < 10) {
            ServiceReference registryRef = bundleContext.getServiceReference("eu.europeana.uim.api.Registry");
            if (registryRef != null) {
                registry = (Registry)bundleContext.getService(registryRef);
            }
            Thread.sleep(1000);
        }

        expengine = new ExpandedOsgiEngine(registry,repoxService,sugarCrmService);
        ExpandedOsgiEngine.setEngine(expengine);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		expengine = null;

	}

}
