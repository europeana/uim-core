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

package eu.europeana.uim.sugarcrmclient.plugin.objects.queries;

import eu.europeana.uim.sugarcrmclient.plugin.objects.data.DatasetStates;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.SugarCrmField;


/**
 * This is a simplified version of a SugarCRM query object. It is used to perform a query 
 * in the "opportunities" module retrieving objects having a specific state.
 * 
 * 
 * @author Georgios Markakis
 */
public class SimpleSugarCrmQuery implements SugarCrmQuery{

	private DatasetStates status;
	
	private SugarCrmField orderBy;
	private int maxResults;
	private int offset;
	
	
	
	/**
	 * Default Constructor
	 */
	public SimpleSugarCrmQuery(){
		//Initialize fields to default values
		this.status = DatasetStates.HARVESTING_PENDING;
		this.orderBy = RetrievableField.DATE_ENTERED;
		this.maxResults = 100;
		this.offset =0 ;
	}
	
	
	
	
	/**
	 * Default Constructor
	 */
	public SimpleSugarCrmQuery(DatasetStates status){
		//Initialize fields to default values
		this.status = status;
		this.orderBy = RetrievableField.DATE_ENTERED;
		this.maxResults = 100;
		this.offset =0 ;
	}
	
	
	@Override
	public String toString(){
		String requestQuery = "(opportunities.sales_stage LIKE '" + status.getSysId() +"')";
		
		return requestQuery;
	}


	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(SugarCrmField orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the orderBy
	 */
	public SugarCrmField getOrderBy() {
		return orderBy;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

}
