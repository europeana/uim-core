/**
 * 
 */
package eu.europeana.uim.util.sugarcrm;

import java.util.List;


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
