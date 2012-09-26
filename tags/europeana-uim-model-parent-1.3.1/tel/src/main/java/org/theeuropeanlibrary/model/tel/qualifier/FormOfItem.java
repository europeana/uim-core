/* BibliographicRecordType.java - created on 29 de Ago de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * FIELD 008 Character 22 and 006 char 5 MAPPING (Continuing Resources): form original item FIELD
 * 008 Character 23 and 006 Char 06 MAPPING (Book, Computer Files, Music, Mixed material, Continuing
 * Resource): Form of Item UNIMARC DATAFIELD 106 $a Char 00 MAPPING: FORM OF ITEM
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 29 de Ago de 2011
 */
public enum FormOfItem {
    /**
     * MARC21 code 'a' UNIMARC code '-'
     */
    MICROFILM,

    /**
     * MARC21 code 'b'
     */
    MICROFICHE,

    /**
     * MARC21 code 'c' UNIMARC code '-'
     */
    MICROOPAQUE,

    /**
     * MARC21 code 'd' UNIMARC code 'd'
     */
    LARGE_PRINT,

    /**
     * MARC21 code 'e' UNIMARC code 'e'
     */
    NEWSPAPER_FORMAT,
    /**
     * MARC21 'f' UNIMARC code 'f'
     */
    BRAILLE,

    /**
     * MARC21 '-' UNIMARC code 'g'
     */
    MICROPRINT,

    /**
     * MARC21 '-' UNIMARC code 'h'
     */
    HAND_WRITTEN,

    /**
     * MARC21 '-' UNIMARC code 'i'(e.g. an item in regular print with a microfiche supplement)
     */
    MULTIMEDIA,

    /**
     * MARC21 '-' UNIMARC code 'j'
     */
    MINIPRINT,

    /**
     * MARC21 code 'o' UNIMARC code '-'
     */
    ONLINE,

    /**
     * MARC21 code 'q' UNIMARC code '-'
     */
    DIRECT_ELECTRONIC,

    /**
     * MARC21 code 'r' UNIMARC code 'r'
     */
    REGULAR_PRINT_REPRODUCTION,

    /**
     * MARC21 code 's' UNIMARC code 's'
     */
    ELECTRONIQUE, /**
     * 
     * MARC21 code '-' UNIMARC code 't'
     */
    MICROFORM, ;

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static FormOfItem fromMarc21Code(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case 'a':
            return MICROFILM;
        case 'b':
            return MICROFICHE;
        case 'c':
            return MICROOPAQUE;
        case 'd':
            return LARGE_PRINT;
        case 'e':
            return NEWSPAPER_FORMAT;
        case 'f':
            return BRAILLE;
        case 'o':
            return ONLINE;
        case 'q':
            return DIRECT_ELECTRONIC;
        case 'r':
            return REGULAR_PRINT_REPRODUCTION;
        case 's':
            return ELECTRONIQUE;
        }
        return null;
    }

    /**
     * @param typeOfRecordCode
     * @return ResourceType
     */
    public static FormOfItem fromUnimarcCode(char typeOfRecordCode) {
        switch (typeOfRecordCode) {
        case 'd':
            return LARGE_PRINT;
        case 'e':
            return NEWSPAPER_FORMAT;
        case 'f':
            return BRAILLE;
        case 'g':
            return MICROPRINT;
        case 'h':
            return HAND_WRITTEN;
        case 'i':
            return MULTIMEDIA;
        case 'j':
            return MINIPRINT;
        case 'r':
            return REGULAR_PRINT_REPRODUCTION;
        case 's':
            return ELECTRONIQUE;
        case 't':
            return MICROFORM;
        }
        return null;
    }
}
