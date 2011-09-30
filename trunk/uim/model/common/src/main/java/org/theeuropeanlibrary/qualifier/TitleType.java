/* TitleType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * Qualifier for titles.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum TitleType {
	/**
	 * main title, how it is used now
	 */
	MAIN,
	/**
	 * uniform title for better transparency
	 */
	UNIFORM,
	/**
	 * another title holding alternative representation
	 */
	ALTERNATIVE,
    /**
     * A title of another resource, which is the subject of this resource
     */
    SUBJECT;
}
