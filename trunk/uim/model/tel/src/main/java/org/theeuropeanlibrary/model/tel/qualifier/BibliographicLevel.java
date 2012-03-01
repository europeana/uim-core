/* BibliographicLevel.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * The bibliographic level of a record relates to the main part of the record, or the primary bibliographic entity described in that record 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum BibliographicLevel {
	
	/** 
	 *	MARC21 code 'a'
	 *  UNIMARC code -
	 */
	ANALITIC_MONOGRAPHIC,
	
	/** 
	 *	MARC21 code 'b'
	 *  UNIMARC code -
	 */
	ANALITIC_SERIAL,
	
	/** 
	 *	MARC21 code -
	 *  UNIMARC code 'a'
	 */
	ANALITIC,
	
	/** 
	 *	MARC21 code 'c'
	 *  UNIMARC code 'c'
	 */
	COLLECTION_MADE_UP,
	
	/** 
	 *	MARC21 code 'd'
	 *  UNIMARC code -
	 */
	SUBUNIT,
	
	/** 
	 *	MARC21 code 'i'
	 *  UNIMARC code -
	 */
	INTEGRATING_RESOURCE,
	
	/** 
	 *	MARC21 code 'm'
	 *  UNIMARC code 'm'
	 */
	MONOGRAPH, 
	
	/** 
	 *	MARC21 code 's'
	 *  UNIMARC code 's'
	 */
	SERIAL;
	
	
	
	/**
	 * @param bibLevelCode
	 * @return BibliographicLevel
	 */
	public static BibliographicLevel fromMarc21Code(char bibLevelCode) {
		switch (bibLevelCode) {
		case 'a':
			return ANALITIC_MONOGRAPHIC;
		case 'b':
			return ANALITIC_SERIAL;
		case 'c':
			return COLLECTION_MADE_UP;
		case 'd':
			return SUBUNIT;
		case 'i':
			return INTEGRATING_RESOURCE;
		case 'm':
			return MONOGRAPH;
		case 's':
			return SERIAL;
		}
		return null;
	}

	/**
	 * @param bibLevelCode
	 * @return BibliographicLevel
	 */
	public static BibliographicLevel fromUnimarcCode(char bibLevelCode) {
		switch (bibLevelCode) {
		case 'a':
			return ANALITIC;
		case 'c':
			return COLLECTION_MADE_UP;
		case 'm':
			return MONOGRAPH;
		case 's':
			return SERIAL;
		}
		return null;
	}
	
	
	/**
	 * @return true if the valeu corresponds to a general continuing resource
	 */
	public boolean isContinuingResource() {
	        switch (this) {
	        case ANALITIC_SERIAL:
	        case SERIAL:
	        case INTEGRATING_RESOURCE:
	            return true;
            default:
                return false;
	        }
	    }
}

