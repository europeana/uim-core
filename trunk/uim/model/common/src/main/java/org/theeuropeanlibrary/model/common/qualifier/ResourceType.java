/* BibliographicRecordType.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Types of content and material 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum ResourceType {
	/** 
	 *	MARC21 code 'a', 't'
	 *  UNIMARC code 'a', 'b'
	 */
	TEXT,
    
	/** 
	 *	MARC21 code 'e', 'f'
	 *  UNIMARC code 'e', 'f'
	 */
	CARTOGRAPHIC,
	
	/** 
	 *	MARC21 code 'c', 'd'
	 *  UNIMARC code 'c', 'd'
	 */
	NOTATED_MUSIC,
	
	/** 
	 *	MARC21 code 'i'
	 *  UNIMARC code 'i'
	 */
	SOUND_RECORDING,
	
	/** 
	 *	MARC21 code 'j'
	 *  UNIMARC code 'j'
	 */
	MUSIC_SOUND_RECORDING,

	/** 
	 *	MARC21 code 'k'
	 *  UNIMARC code 'k'
	 */
	STILL_IMAGE,
	
	/** 
	 *	MARC21 code 'g'
	 *  UNIMARC code 'g'
	 */
	VIDEO,
	
	/** 
	 *	MARC21 code 'r'
	 *  UNIMARC code 'r'
	 */
	THREE_DIMENSIONAL_OBJECT,
	
	/** 
	 *	MARC21 code 'm'
	 *  UNIMARC code 'l'
	 */
	ELECTRONIC_RESOURCE,
	
	/** 
	 *	MARC21 code -
	 *  UNIMARC code 'm'
	 */
	MULTIMEDIA,

	/** 
	 *	MARC21 code 'p'
	 *  UNIMARC code -
	 */
	MIXED_MATERIALS;
	
	
	/**
	 * @return is Visual Material
	 */
	public boolean isVisualMaterial() {
	    return this==VIDEO || this==STILL_IMAGE || this==MIXED_MATERIALS ||  this==THREE_DIMENSIONAL_OBJECT || this==MULTIMEDIA;
	}
	
	/**
	 * @return is a Book or manuscript
	 */
	public boolean isBook() {
	    return this==TEXT ;
	}
	
	/**
	 * @return is a map
	 */
	public boolean isMap() {
	    return this==CARTOGRAPHIC ;
	}

	/**
	 * @return is sound or music
	 */
	public boolean isSound() {
	    return this==MUSIC_SOUND_RECORDING || this==NOTATED_MUSIC || this==SOUND_RECORDING;
	}

	/**
	 * @param typeOfRecordCode
	 * @return ResourceType
	 */
	public static ResourceType fromMarc21Code(char typeOfRecordCode) {
		switch (typeOfRecordCode) {
		case 'a':
			return TEXT;
		case 't':
			return TEXT;
		case 'e':
			return CARTOGRAPHIC;
		case 'f':
			return CARTOGRAPHIC;
		case 'c':
			return NOTATED_MUSIC;
		case 'd':
			return NOTATED_MUSIC;
		case 'i':
			return SOUND_RECORDING;
		case 'j':
			return MUSIC_SOUND_RECORDING;
		case 'k':
			return STILL_IMAGE;
		case 'g':
			return VIDEO;
		case 'r':
			return THREE_DIMENSIONAL_OBJECT;
		case 'm':
			return ELECTRONIC_RESOURCE;
		case 'o'://KIT
		case 'p':
			return MIXED_MATERIALS;
		}
		return null;
	}
	
	
	/**
	 * @param typeOfRecordCode
	 * @return ResourceType
	 */
	public static ResourceType fromUnimarcCode(char typeOfRecordCode) {
		switch (typeOfRecordCode) {
		case 'a':
			return TEXT;
		case 'b':
			return TEXT;
		case 'e':
			return CARTOGRAPHIC;
		case 'f':
			return CARTOGRAPHIC;
		case 'c':
			return NOTATED_MUSIC;
		case 'd':
			return NOTATED_MUSIC;
		case 'i':
			return SOUND_RECORDING;
		case 'j':
			return MUSIC_SOUND_RECORDING;
		case 'k':
			return STILL_IMAGE;
		case 'g':
			return VIDEO;
		case 'r':
			return THREE_DIMENSIONAL_OBJECT;
		case 'l':
			return ELECTRONIC_RESOURCE;
		case 'm':
			return MULTIMEDIA;
		}
		return null;
	}
}
