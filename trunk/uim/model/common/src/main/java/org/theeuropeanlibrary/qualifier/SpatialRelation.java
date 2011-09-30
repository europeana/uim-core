/* SpatialRelation.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * Disambiguates between purposes of a specific party like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public enum SpatialRelation {
    /**
     * The place where a resource was published
     */
    PUBLICATION,
    /**
     * The place is the subject of the resource
     */
    SUBJECT
}
