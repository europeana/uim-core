/* AgentRelation.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model.qualifier;

/**
 * Disambiguates between the level of a specific concepts 
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 18, 2011
 */
public enum ConceptLevel {
    /**
     * level of application - aggregation
     */
    AGGREGATION,
    /**
     * level of application - proxy
     */
    PROXY,
    /**
     * level of application - object
     */
    OBJECT,
}
