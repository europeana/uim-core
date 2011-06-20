/**
 * 
 */
package eu.europeana.uim.repoxclient.rest.exceptions;

import eu.europeana.uim.repoxclient.jibxbindings._Error;

/**
 * An exception thrown in case of a Aggregator related operation error 
 * (create, update or delete aggregator).
 *  
 * @author Georgios Markakis
 */
public class AggregatorOperationException extends RepoxException {

	private static final long serialVersionUID = 1L;

	/**
	 * This constructor takes as an argument a String
	 * @param message the error message
	 */
	public AggregatorOperationException(String message) {
		super(message);
	}

	/**
	 * This constructor takes as an argument an _Error object
	 * @param err
	 */
	public AggregatorOperationException(_Error err) {
		super(err);
	}
}
