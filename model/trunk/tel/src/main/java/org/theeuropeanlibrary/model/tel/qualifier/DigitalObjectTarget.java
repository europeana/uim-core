/* DigitalObjectTarget.java - created on 27/11/2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * Further allows the qualification of a LinkTarget.DIGITAL_OBJECT to support the europeana requirements  
 * for isShownAt and isShownBy
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 27/11/2013
 */
public enum DigitalObjectTarget {

    /**
     * a link to the digital object shown in context (same as europeana:isShownAt)
     */
    IS_SHOWN_AT,
    /**
     * a direct link to the digital object (same as europeana:isShownBy)
     */
    IS_SHOWN_BY,
    /**
     * a link to the digital object shown in context (same as europeana:isShownAt)
     */
    HAS_VIEW,
    /**
     * a digital link that is only useful for TEL
     */
    TEL
}
