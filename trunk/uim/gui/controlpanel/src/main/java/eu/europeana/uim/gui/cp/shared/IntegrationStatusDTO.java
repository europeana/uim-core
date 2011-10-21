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
 * Client Side Object for representing Client status information
 * 
 * @author Georgios Markakis
 */
public class IntegrationStatusDTO implements IsSerializable {

	public static enum TYPE{
		PROVIDER,
		COLLECTION,
		UNIDENTIFIED
	}
	

	
	private String id;
	
	private TYPE type;
	
	private String sugarCRMID;
	
	private String repoxID;
	
	private HarvestingStatusDTO harvestingStatus;
	
	private String info;

	private String state;
	
	private String repoxURL;
	
	private String sugarURL;
	
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id 
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TYPE type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * @param sugarCRMID the sugarCRMID to set
	 */
	public void setSugarCRMID(String sugarCRMID) {
		this.sugarCRMID = sugarCRMID;
	}

	/**
	 * @return the sugarCRMID
	 */
	public String getSugarCRMID() {
		return sugarCRMID;
	}

	/**
	 * @param repoxID the repoxID to set
	 */
	public void setRepoxID(String repoxID) {
		this.repoxID = repoxID;
	}

	/**
	 * @return the repoxID
	 */
	public String getRepoxID() {
		return repoxID;
	}

	/**
	 * @param harvestingStatus the harvestingStatus to set
	 */
	public void setHarvestingStatus(HarvestingStatusDTO harvestingStatus) {
		this.harvestingStatus = harvestingStatus;
	}

	/**
	 * @return the harvestingStatus
	 */
	public HarvestingStatusDTO getHarvestingStatus() {
		return harvestingStatus;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param repoxURL the repoxURL to set
	 */
	public void setRepoxURL(String repoxURL) {
		this.repoxURL = repoxURL;
	}

	/**
	 * @return the repoxURL
	 */
	public String getRepoxURL() {
		return repoxURL;
	}

	/**
	 * @param sugarURL the sugarURL to set
	 */
	public void setSugarURL(String sugarURL) {
		this.sugarURL = sugarURL;
	}

	/**
	 * @return the sugarURL
	 */
	public String getSugarURL() {
		return sugarURL;
	}
	
	
	
}
