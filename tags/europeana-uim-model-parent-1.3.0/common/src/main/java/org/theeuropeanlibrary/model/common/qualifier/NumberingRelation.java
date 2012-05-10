/* NumberingType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Qualifier for numberings.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum NumberingRelation {
    /**
     * The volume number for multi volume resources
     */
    VOLUME,
    /**
     * The issue number of a periodic publication
     */
    ISSUE,
    /**
     * A specific page number
     */
    PAGE
}
