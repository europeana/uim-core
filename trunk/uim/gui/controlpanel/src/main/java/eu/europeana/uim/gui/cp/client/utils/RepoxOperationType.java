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
package eu.europeana.uim.gui.cp.client.utils;

/**
 * Enumeration describing the available REPOX operation 
 * types that can be invoked directly from within UIM. 
 * 
 * @author Georgios Markakis
 */
public enum RepoxOperationType {

	VIEW_HARVEST_LOG("View Harvest Log"),
	INITIATE_COMPLETE_HARVESTING("Initiate Harvesting (Complete)"),
	INITIATE_INCREMENTAL_HARVESTING("Initiate Harvesting (Incremental)"),
	SCHEDULE_HARVESTING("Schedule Harvesting")
	;
	
	
	private final String description;	
	
	
	/**
	 * @param description
	 */
	RepoxOperationType(String description){
		
		this.description = description;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
