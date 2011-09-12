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

import java.util.logging.Logger;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.sugarcrm.SugarCrmService;

/**
 * Expanded version of the OsgiEngine giving access to SugarCRM and Repox services
 * 
 * @author Georgios Markakis
 */
public class ExpandedOsgiEngine extends OsgiEngine {

    private static Logger log = Logger.getLogger(ExpandedOsgiEngine.class.getName());
	
	
	    private final RepoxUIMService repoxService;
	    
	    private final SugarCrmService sugarCrmService;

	    /**
	     * Creates a new instance of this class.
	     * 
	     * @param registry
	     */
	    public ExpandedOsgiEngine(Registry registry,RepoxUIMService repoxService,SugarCrmService sugarCrmService) {
	    	super(registry);
	       this.repoxService = repoxService;
	       this.sugarCrmService = sugarCrmService;
	    }

		/**
		 * @return the repoxService
		 */
		public RepoxUIMService getRepoxService() {
			return repoxService;
		}

		/**
		 * @return the sugarCrmService
		 */
		public SugarCrmService getSugarCrmService() {
			return sugarCrmService;
		}
		
		
	    /**
	     * @return singleton instance of engine.
	     */
	    public static ExpandedOsgiEngine getInstance() {
			
	    	return (ExpandedOsgiEngine) Engine.getInstance();
	    }

	    /**
	     * @param newInstance
	     *            sets an engine instance to be used throughout the application
	     */
	    protected static void setEngine(ExpandedOsgiEngine newInstance) {
	    	Engine.setEngine(newInstance);
	    }



}
