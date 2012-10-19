/* PrintType.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * Target audience of a resource
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum Audience {
    /**
     * Marc21 ' ', '|' Unimarc 'u'
     */
    UNKNOW,

    /**
     * ages 0-5 Marc21 'a' Unimarc 'b'
     */
    PRESCHOOL,

    /**
     * ages 5-10 Marc21 'b' Unimarc 'c'
     */
    PRIMARY,

    /**
     * ages 9-14 Marc21 'c' Unimarc 'd'
     */
    CHILDREN,

    /**
     * ages 14-20 Marc21 'd' Unimarc 'e'
     */
    ADOLESCENT,

    /**
     * Marc21 'g' Unimarc 'm'
     */
    ADULT_GENERAL,

    /**
     * Marc21 'e' Unimarc 'k'
     */
    ADULT_SERIOUS,

    /**
     * ages 0-20 Marc21 'j' Unimarc 'a'
     */
    JUVENILE,

    /**
     * ages 0-20 Marc21 'f' Unimarc -
     */
    SPECIALIZED;

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static Audience fromMarc21Code(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case ' ':
        case '|':
            return UNKNOW;
        case 'a':
            return PRESCHOOL;
        case 'b':
            return PRIMARY;
        case 'c':
            return CHILDREN;
        case 'd':
            return ADOLESCENT;
        case 'g':
            return ADULT_GENERAL;
        case 'e':
            return ADULT_SERIOUS;
        case 'j':
            return JUVENILE;
        case 'f':
            return SPECIALIZED;
        }
        return null;
    }

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static Audience fromUnimarcCode(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case 'u':
        case 'b':
            return PRESCHOOL;
        case 'c':
            return PRIMARY;
        case 'd':
            return CHILDREN;
        case 'e':
            return ADOLESCENT;
        case 'm':
            return ADULT_GENERAL;
        case 'k':
            return ADULT_SERIOUS;
        case 'a':
            return JUVENILE;
        }
        return null;
    }
}
