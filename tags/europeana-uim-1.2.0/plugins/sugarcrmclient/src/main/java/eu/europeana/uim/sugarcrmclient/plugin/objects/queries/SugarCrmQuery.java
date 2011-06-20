/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.plugin.objects.queries;

import java.util.List;

import eu.europeana.uim.sugarcrmclient.plugin.objects.data.SugarCrmField;

/**
 * The interface daclaration of a SugarCrmQuery
 * 
 * @author Georgios Markakis
 */
public interface SugarCrmQuery {

	
	public void setOrderBy(SugarCrmField orderBy);
	
	public SugarCrmField getOrderBy();
	
	public void setMaxResults(int maxResults);
	
	public int getMaxResults();
	
	public void setOffset(int offset);
	
	public int getOffset(); 
	
}
