/* Illustrations.java - created on 13 de Mar de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * Illustrations contained in the resource
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 13 de Mar de 2012
 */
public enum Illustrations {
    /**
     * No illustrations marc21 code '#'
     */
    NO_ILLUSTRATIONS,

    /**
     * Illustrations marc21 code 'a'
     */
    ILLUSTRATIONS,

    /**
     * Maps marc21 code 'b'
     */
    MAPS,

    /**
     * Portraits marc21 code 'c'
     */
    PORTRAITS,

    /**
     * Charts marc21 code 'd'
     */
    CHARTS,

    /**
     * Plans marc21 code 'e'
     */
    PLANS,

    /**
     * Plates marc21 code 'f'
     */
    PLATES,

    /**
     * Music marc21 code 'g'
     */
    MUSIC,

    /**
     * Facsimiles marc21 code 'h'
     */
    FACSIMILES,

    /**
     * Coats of arms marc21 code 'i'
     */
    COATS_OF_ARMS,

    /**
     * Genealogical tables marc21 code 'j'
     */
    GENEALOGICAL_TABLES,

    /**
     * Forms marc21 code 'k'
     */
    FORMS,

    /**
     * Samples marc21 code 'l'
     */
    SAMPLES,

    /**
     * Phonodisc, phonowire, etc. marc21 code 'm'
     */
    PHONODISC_PHONOWIRE,

    /**
     * Photographs marc21 code 'o'
     */
    PHOTOGRAPHS,

    /**
     * Illuminations marc21 code 'p'
     */
    ILLUMINATIONS;

    /**
     * @param marc21Code
     * @return MultipartResourceRecordLevel
     */
    public static Illustrations fromMarc21Code(char marc21Code) {
        switch (marc21Code) {
        case ' ':
        case '#':
            return NO_ILLUSTRATIONS;
        case 'a':
            return ILLUSTRATIONS;
        case 'b':
            return MAPS;
        case 'c':
            return PORTRAITS;
        case 'd':
            return CHARTS;
        case 'e':
            return PLANS;
        case 'f':
            return PLATES;
        case 'g':
            return MUSIC;
        case 'h':
            return FACSIMILES;
        case 'i':
            return COATS_OF_ARMS;
        case 'j':
            return GENEALOGICAL_TABLES;
        case 'k':
            return FORMS;
        case 'l':
            return SAMPLES;
        case 'm':
            return PHONODISC_PHONOWIRE;
        case 'o':
            return PHOTOGRAPHS;
        case 'p':
            return ILLUMINATIONS;
        }
        return null;
    }
}
