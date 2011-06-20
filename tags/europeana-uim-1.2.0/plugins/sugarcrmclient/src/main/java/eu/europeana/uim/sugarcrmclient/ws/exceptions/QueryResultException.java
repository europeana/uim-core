/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.ws.exceptions;

import eu.europeana.uim.sugarcrmclient.jibxbindings.ErrorValue;

/**
 * This exception occurs when the returned message indicates an error
 * in the search process.
 * 
 * @author Georgios Markakis
 */
public class QueryResultException extends GenericSugarCRMException {

	private static final long serialVersionUID = 1L;

	/**
	 * This constructor takes as an argument an ErrorValue object
	 * @param err the ErrorValue message
	 */
	public QueryResultException(ErrorValue err) {
		
		super(generateMessageFromObject(err));

	}
}
