/* LanguageRelation.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Specifies the type of relation of a resource with a language
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 30, 2011
 */
public enum LanguageRelation {
    /**
     * The language of the text of the resource
     */
    LANGUAGE_OF_CONTENT,
    /**
     * The language used in cataloging, or in the creation of the metadata
     */
    LANGUAGE_OF_CATALOGUING,
    /**
     * The language of the original the resource (used in translations of works)
     */
    LANGUAGE_OF_ORIGINAL
}
