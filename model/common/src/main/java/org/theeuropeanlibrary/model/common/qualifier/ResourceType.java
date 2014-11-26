/* BibliographicRecordType.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * Types of content and material
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum ResourceType {
    /**
     * MARC21 code 'a', 't' UNIMARC code 'a', 'b'
     */
    TEXT,

    /**
     * MARC21 code 'e', 'f' UNIMARC code 'e', 'f'
     */
    CARTOGRAPHIC,

    /**
     * MARC21 code 'c', 'd' UNIMARC code 'c', 'd'
     */
    NOTATED_MUSIC,

    /**
     * MARC21 code 'i' UNIMARC code 'i'
     */
    SOUND_RECORDING,

    /**
     * MARC21 code 'j' UNIMARC code 'j'
     */
    MUSIC_SOUND_RECORDING,

    /**
     * MARC21 code 'k' UNIMARC code 'k'
     */
    STILL_IMAGE,

    /**
     * MARC21 code 'g' UNIMARC code 'g'
     */
    VIDEO,

    /**
     * MARC21 code 'r' UNIMARC code 'r'
     */
    THREE_DIMENSIONAL_OBJECT,

    /**
     * MARC21 code 'm' UNIMARC code 'l'
     */
    ELECTRONIC_RESOURCE,

    /**
     * MARC21 code - UNIMARC code 'm'
     */
    MULTIMEDIA,

    /**
     * MARC21 code 'p' UNIMARC code -
     */
    MIXED_MATERIALS;

    
    final String humanReadableLabel;
    
    /**
     * Creates a new instance of this class.
     */
    private ResourceType() {
        humanReadableLabel=WordUtils.capitalize(name().replace('_', ' ').toLowerCase());
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
        case 'o':// KIT
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
    

    /**
     * @return
     */
    public String toHumanReadableLabel() {
        return humanReadableLabel;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(THREE_DIMENSIONAL_OBJECT.toHumanReadableLabel());
    }
}
