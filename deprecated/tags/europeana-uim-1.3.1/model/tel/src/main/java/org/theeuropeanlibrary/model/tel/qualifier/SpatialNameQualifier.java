/* NameQualifier.java - created on 31 de Out de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * Qualifier for toponym names
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 31 de Out de 2011
 */
public enum SpatialNameQualifier {
	/** 
	 * The name is the primary name of the toponym		
	 */
	MAIN,
	/** 
	 * The name is an alternative name of the toponym
	 */
	ALTERNATIVE,
	/** 
	 * The name is a short name of the toponym
	 */
	SHORT,
	/** 
	 * The name is an official name of the toponym
	 */
	OFFICIAL;
}
