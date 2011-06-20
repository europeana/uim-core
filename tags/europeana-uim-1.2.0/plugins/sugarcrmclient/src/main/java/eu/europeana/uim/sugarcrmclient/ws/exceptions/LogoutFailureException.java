/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.ws.exceptions;

import eu.europeana.uim.sugarcrmclient.jibxbindings.ErrorValue;

/**
 * Exception thrown in case of a Logout Error
 * 
 * @author Georgios Markakis
 */
public class LogoutFailureException extends GenericSugarCRMException {

	private static final long serialVersionUID = 1L;

	/**
	 * This constructor takes as an argument an ErrorValue object
	 * @param err the ErrorValue message
	 */
	public LogoutFailureException(String err) {
		
		super(err);

	}
}
