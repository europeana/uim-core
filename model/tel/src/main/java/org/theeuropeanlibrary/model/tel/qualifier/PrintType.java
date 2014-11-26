/* PrintType.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

import org.apache.commons.lang.WordUtils;

/**
 * If a resource is printed or is a manuscript
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum PrintType {
    /**
     * Printed resource
     */
    PRINTED,

    /**
     * Manuscript
     */
    MANUSCRIPT;

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static PrintType fromMarc21Code(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case 'a':
            return PRINTED;
        case 't':
            return MANUSCRIPT;
        case 'e':
            return PRINTED;
        case 'f':
            return MANUSCRIPT;
        case 'c':
            return PRINTED;
        case 'd':
            return MANUSCRIPT;
        }
        return null;
    }

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static PrintType fromUnimarcCode(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case 'a':
            return PRINTED;
        case 'b':
            return MANUSCRIPT;
        case 'e':
            return PRINTED;
        case 'f':
            return MANUSCRIPT;
        case 'c':
            return PRINTED;
        case 'd':
            return MANUSCRIPT;
        }
        return null;
    }

    final String humanReadableLabel;
    
    /**
     * Creates a new instance of this class.
     */
    private PrintType() {
        humanReadableLabel=WordUtils.capitalize(name().replace('_', ' ').toLowerCase());
    }
    
    /**
     * @return
     */
    public String toHumanReadableLabel() {
        return humanReadableLabel;
    }
}
