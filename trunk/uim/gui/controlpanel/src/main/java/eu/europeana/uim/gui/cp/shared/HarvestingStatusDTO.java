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
package eu.europeana.uim.gui.cp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Client Side object for Harvesting Status Information
 * 
 * @author Georgios Markakis
 */
public class HarvestingStatusDTO implements IsSerializable {

	public static enum STATUS{
		RUNNING("Harvesting Under Process.."),
		ERROR("An error Occured during harvesting.See the Error Log."),
		OK("Harvesting Completed"),
		WARNING("Harvesting Completed with warnings.See the Error Log."),
		CANCELLED("Harvesting Cancelled"),
		UNDEFINED("Harvesting Not Initialized"),
		SYSTEM_ERROR("Harvesting Encountered System Errors. See the Error Log. ");
		
		private final String description;	
		
		 STATUS(String description){
		   this.description = description;
		}
		 
		public String getDescription() {
				return description;
		}
	}
	
	private STATUS status;

	/**
	 * @param status the status to set
	 */
	public void setStatus(STATUS status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	} 
	
}
