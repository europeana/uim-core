/* Status.java - created on 19 de Mai de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * The status of a metadata record or field. Currently it is only used for record status.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 19 de Mai de 2011
 */
public enum Status {
    /**
     * has been created (intial storage)
     */
    CREATED,
    /**
     * has been updated
     */
    UPDATED,
    /**
     * has been deleted
     */
    DELETED,
    /**
     * has been cleaned from internal tracking
     */
    CLEANUP;

    /**
     * @param bibLevelCode
     * @return status depending on bib level code
     */
    public static Status fromMarc21Code(char bibLevelCode) {
        switch (bibLevelCode) {
        case 'a':
            return UPDATED;
        case 'c':
            return UPDATED;
        case 'd':
            return DELETED;
        case 'n':
            return CREATED;
        case 'p':
            return UPDATED;
        }
        return null;
    }
}
