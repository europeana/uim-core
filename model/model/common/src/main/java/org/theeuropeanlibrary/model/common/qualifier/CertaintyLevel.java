/* CertaintyLevel.java - created on Aug 13, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Disambiguates between different levels of certainty for automatically enriched values. This is
 * human-readable certainty with a limited set of values. It is up to the implementation to decide
 * the level of certainty, but it should follow the semantics of the different enum texts.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Aug 13, 2012
 */
public enum CertaintyLevel {
    /**
     * certainty is definitive, for example id matching should be defintive
     */
    DEFINITIVE,
    /**
     * highly likely is an assurance of almost 100%, automatic approaches with almost hundret
     * percent certainty should use this
     */
    HIGHLY_LIKELY,
    /**
     * likely by all means of an automatic approach
     */
    LIKELY,
    /**
     * enriched value has been chosen as a very good candidate and has been selected as best match
     */
    EDUCATED_GUESS,
    /**
     * enriched value has some evidence to be correct, but far from certainty
     */
    GUESS,
    /**
     * enriched value is just a candidate to be a correct guess
     */
    CANDIDATE
}
