/* NameQualifier.java - created on 31 de Out de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.spatial;

/**
 * Qualifier for coordinates
 * (currently only supports the geographic center of the toponym, but in the future it may also support bounding boxes, or poligons)
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 31 de Out de 2011
 */
public enum CoordinateQualifier {
	/** 
	 * The longitude of the geographic center		
	 */
	CENTER_LONGITUDE,

	/** 
	 * The latitude of the geographic center		
	 */
	CENTER_LATITUDE,
	
}
