/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.plugin.objects.queries;

import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;

/**
 * @author Georgios Markakis
 *
 */
public class CustomSugarCrmQuery extends SimpleSugarCrmQuery {

	private String querystring;
	
	
	public CustomSugarCrmQuery(String querystring){
		this.querystring = querystring;
	}

	/**
	 * @param querystring the querystring to set
	 */
	public void setQuerystring(String querystring) {
		this.querystring = querystring;
	}

	/**
	 * @return the querystring
	 */
	public String getQuerystring() {
		return querystring;
	}
	
	@Override
	public String toString(){
		String requestQuery = "(" + querystring + ")";
		
		return requestQuery;
	}
}
