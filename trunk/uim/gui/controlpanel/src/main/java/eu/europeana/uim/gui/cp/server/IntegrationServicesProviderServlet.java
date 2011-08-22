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
package eu.europeana.uim.gui.cp.server;

import eu.europeana.uim.gui.cp.server.engine.Engine;
import eu.europeana.uim.gui.cp.server.engine.ExpandedOsgiEngine;

/**
 * 
 * 
 * @author Georgios Markakis
 */
public class IntegrationServicesProviderServlet extends
		AbstractOSGIRemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final ExpandedOsgiEngine        engine;

    /**
     * Creates a new instance of this class.
     */
    public IntegrationServicesProviderServlet() {
        this.engine = ExpandedOsgiEngine.getInstance();
    }
    
    /**
     * @return implementation of an engine
     */
    @Override
    protected ExpandedOsgiEngine getEngine() {
        if (engine == null) { throw new RuntimeException(
                "No engine found. Make sure the OSGi platform is running and all UIM modules are started"); }
        return engine;
    }
    
}
