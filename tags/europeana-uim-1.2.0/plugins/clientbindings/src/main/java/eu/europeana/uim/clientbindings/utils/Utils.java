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
package eu.europeana.uim.clientbindings.utils;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;



/**
 * Class providing utilities for binding objects 
 * 
 * @author Georgios Markakis
 */
public class Utils {

	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(Utils.class);

	
	
	/**
	 * This method marshals the contents of a  JIBX Element and outputs the results to the
	 * Logger.  
	 * @param jaxbObject A JIBX representation of a SugarCRM SOAP Element. 
	 */
	public static void logMarshalledObject(Object jibxObject){		
		IBindingFactory context;

		try {
			context = BindingDirectory.getFactory(jibxObject.getClass());

			IMarshallingContext mctx = context.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter stringWriter = new StringWriter();
			mctx.setOutput(stringWriter);
			mctx.marshalDocument(jibxObject);
			
			LOGGER.info("===========================================");
			StringBuffer sb = new StringBuffer("Soap Ouput for Class: ");
			sb.append(jibxObject.getClass().getSimpleName());
			LOGGER.info(sb.toString());
			LOGGER.info(stringWriter.toString());
			LOGGER.info("===========================================");
		} catch (JiBXException e) {

			e.printStackTrace();
		}

		
	}
}
