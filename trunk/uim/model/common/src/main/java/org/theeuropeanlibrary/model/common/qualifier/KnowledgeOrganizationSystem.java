/* TopicIdentifierType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Knowledge Organization Systems (Subject systems, classification systems, thesaurus, etc.)
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum KnowledgeOrganizationSystem {
    /**
     * The TEL subjects system (does not exist at this time)
     */
    TEL,
    /**
     * The the national library authority file
     */
    LIBRARY_AUTHORITY_FILE,
    /**
     * Id in MACS if the place was used as a subject
     */
    MACS,
    /**
     * Library of Congress Subject Headings
     */
    LCSH,
    /**
     * Repertoire d'autorite-matiere encyclopedique et alphabetique unifie (France)
     */
    RAMEAU,
    /**
     * Spanish Subject Headings
     */
    CSIC,
    /**
     * Schlagwortnormdatei (Germany)
     */
    SWD,
    /**
     * Medical Subject Headings
     */
    MESH,
    /**
     * Dewey Decimal Classification
     */
    DDC,
    /**
     * Internet Assigned Numbers Authority
     */
    IMT,
    /**
     * Library of Congress Classification
     */
    LCC,
    /**
     * National Library of Medicine Classification
     */
    NLM,
    /**
     * Universal Decimal Classification
     */
    UDC,
    /**
     * Subject headings from Spain
     */
    EMBNE,
    /**
     * Id in CERIF if mapped from 
     */
    CERIF,
    /**
     * Id in used to identify clusters of manfiests
     */
    MANIFEST_GROUP,
}
