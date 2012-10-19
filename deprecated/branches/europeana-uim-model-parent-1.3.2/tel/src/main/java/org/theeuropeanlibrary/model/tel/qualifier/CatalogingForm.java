/* CatalogingForm.java - created on 13 de Mar de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * The rules the cataloguer followed when creating the record. This information is relevant for data
 * cleaning tasks, suvh as the removal of ISBD punctuation, parsing of titles, etc.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 13 de Mar de 2012
 */
public enum CatalogingForm {
    /** CatalogingForm NON_ISBD */
    NON_ISBD,

    /** CatalogingForm AACR2 */
    AACR2,

    /** CatalogingForm ISBD */
    ISBD,

    /** CatalogingForm PARTIAL_ISBD */
    PARTIAL_ISBD,

    /** CatalogingForm PROVISIONAL */
    PROVISIONAL;

    /**
     * @param marc21Code
     * @return Cataloging form
     */
    public static CatalogingForm fromMarc21Code(char marc21Code) {
        if (marc21Code == ' ') return NON_ISBD;
        if (marc21Code == 'a') return AACR2;
        if (marc21Code == 'i') return ISBD;
        if (marc21Code == 'p') return PARTIAL_ISBD;
        if (marc21Code == 'r') return PROVISIONAL;
        return null;
    }
}
