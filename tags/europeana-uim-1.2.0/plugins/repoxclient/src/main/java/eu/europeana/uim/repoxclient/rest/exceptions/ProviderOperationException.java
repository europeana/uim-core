/**
 * 
 */
package eu.europeana.uim.repoxclient.rest.exceptions;

import eu.europeana.uim.repoxclient.jibxbindings._Error;

/**
 * An exception thrown in case of a Provider related operation error 
 * (create, update or delete provider).
 *  
 * @author Georgios Markakis
 */
public class ProviderOperationException extends RepoxException {

	private static final long serialVersionUID = 1L;

	/**
	 * This constructor takes as an argument a String
	 * @param message the error message
	 */
	public ProviderOperationException(String message) {
		super(message);
	}

	/**
	 * This constructor takes as an argument an _Error object
	 * @param err
	 */
	public ProviderOperationException(_Error err) {
		super(err);
	}
}
