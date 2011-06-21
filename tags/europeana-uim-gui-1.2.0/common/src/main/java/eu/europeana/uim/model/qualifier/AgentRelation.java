/* AgentRelation.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model.qualifier;

/**
 * Disambiguates between purposes of a specific agent like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 18, 2011
 */
public enum AgentRelation {
    /**
     * agent created the work at hand
     */
    CREATOR,
    /**
     * agent contributed to the content of the work
     */
    CONTRIBUTOR,
    /**
     * agent was responsible for publishing
     */
    PUBLISHER,
    /**
     * agent was responsible for the manufacture
     */
    MANUFACTURER,
    /**
     * agent is the subject of the resource
     */
    SUBJECT;
}
