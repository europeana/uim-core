/* ContributionType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * Disambiguates between purposes of a specific party like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public enum ContributionType {
    /**
     * party contributed to the content of the work with supplementary text
     */
    AUTHOR_OF_SUPPLEMENTARY_TEXT,
    /**
     * party contributed to the content of the work as a translator
     */
    TRANSLATOR,
    /**
     * party contributed to the content of the work as an illustrator
     */
    ILLUSTRATOR
}
