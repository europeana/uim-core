/* FullText.java - created on May 17, 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

/**
 * This entry represents full - text.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 17, 2013
 */
public class FullText extends Text {
    /**
     * Creates a new instance of this class.
     */
    public FullText() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param textualContent
     *            textual content
     */
    public FullText(String textualContent) {
        super(textualContent);
    }
}
