/* PartyRelation.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Disambiguates between purposes of a specific party like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public enum PartyRelation {
    /**
     * party created the work at hand
     */
    CREATOR,
    /**
     * party contributed to the content of the work
     */
    CONTRIBUTOR,
    /**
     * party was responsible for publishing
     */
    PUBLISHER,
    /**
     * party was responsible for providing the metadata to TEL or an intermediary aggerator
     */
    PROVENANCE,
    /**
     * party was responsible for aggregating the metadata before TEL aggregated it
     */
    PROVENANCE_AGGREGATOR,
    /**
     * party was responsible for the manufacture
     */
    MANUFACTURER,
    /**
     * party is the subject of the resource
     */
    SUBJECT,
    /**
     * party contributed to the content of the series the work is part of
     */
    SERIES_CONTRIBUTOR,
}
