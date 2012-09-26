/* Status.java - created on 19 de Mai de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * The status of a metadata record or field. Currently it is only used for record status.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 19 de Mai de 2011
 */
public enum Maturity {
    /**
     * is accepted
     */
    ACCEPT,
    /**
     * is accepted
     */
    WEAK_ACCEPT,
    /**
     * is accepted
     */
    BORDERLINE,
    /**
     * is accepted
     */
    WEAK_REJECT,
    /**
     * is rejected
     */
    REJECT
}
