/* IdentifierRelation.java - created on 31 de Out de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.spatial;

/**
 * Relation between an identifier and a toponym
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 31 de Out de 2011
 */
public enum IdentifierRelation {
	/** 
	 *	The identifier identifies the toponym itself
	 */
	TOPONYM,
	
	/** 
	 *	The identifier identifies the first order administrative division
	 */
	ADMINISTRATIVE_DIVISION_1,
	
	/** 
	 *	The identifier identifies the second order administrative division
	 */
	ADMINISTRATIVE_DIVISION_2,
	
	/** 
	 *	The identifier identifies the third order administrative division
	 */
	ADMINISTRATIVE_DIVISION_3,
	
	/** 
	 *	The identifier identifies the fourth order administrative division
	 */
	ADMINISTRATIVE_DIVISION_4,

	/** 
	 *	The identifier identifies the type of geographic feature
	 */
	FEATURE
}
