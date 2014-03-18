/* ResourceRelation.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Disambiguates between purposes of a specific party like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public enum ResourceRelation {
    /**
     * see dc:relation
     */
    RELATION,
    /**
     * see dcterms
     */
    IS_VERSION_OF,
    /**
     * see dcterms
     */
    HAS_VERSION,
    /**
     * see dcterms
     */
    IS_REPLACED_BY,
    /**
     * see dcterms
     */
    REPLACES,
    /**
     * see dcterms
     */
    IS_REQUIRED_BY,
    /**
     * see dcterms
     */
    REQUIRES,
    /**
     * see dcterms
     */
    IS_PART_OF,
    /**
     * see dcterms
     */
    HAS_PART,
    /**
     * see dcterms
     */
    IS_REFERENCED_BY,
    /**
     * see dcterms
     */
    REFERENCES,
    /**
     * see dcterms
     */
    IS_FORMAT_OF,
    /**
     * see dcterms
     */
    HAS_FORMAT,
    /**
     * see dcterms
     */
    CONFORMS_TO

}
