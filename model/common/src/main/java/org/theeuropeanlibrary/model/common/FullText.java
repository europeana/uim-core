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
     * object representation of the full-text
     */
    private transient Object object;

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

    /**
     * @return object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object
     */
    public void setObject(Object object) {
        this.object = object;
    }
}
