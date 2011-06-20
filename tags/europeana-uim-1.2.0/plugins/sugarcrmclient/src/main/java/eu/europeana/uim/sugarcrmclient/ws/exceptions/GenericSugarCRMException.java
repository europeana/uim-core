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
package eu.europeana.uim.sugarcrmclient.ws.exceptions;

import eu.europeana.uim.sugarcrmclient.jibxbindings.ErrorValue;

/**
 *  A generic Class representing an unclassified
 *  SugarCRM exception
 * 
 * @author Georgios Markakis
 *
 */
public class GenericSugarCRMException extends Exception {

	private static final long serialVersionUID = 1L;


	/**
	 *  The default constructor
	 */
	public GenericSugarCRMException() {
	
	}

	/**
	 * 
	 * @param message
	 */
	public GenericSugarCRMException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public GenericSugarCRMException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GenericSugarCRMException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Auxiliary method method accessible from all subclasses of this exception type.
	 * It is used to formulate the exception description message... 
	 * 
	 * @param number
	 * @param name
	 * @param description
	 * @return
	 */
	static String generateMessageFromObject(ErrorValue err){ 
		StringBuffer sb = new StringBuffer();
		sb.append("Error Number: ");
		sb.append(err.getNumber());
		sb.append(" Error Name: ");
		sb.append(err.getName());
		sb.append(" Error Description: ");
		sb.append(err.getDescription());
		
		return sb.toString();
	}

}
