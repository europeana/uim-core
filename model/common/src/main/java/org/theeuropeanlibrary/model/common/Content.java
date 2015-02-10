/* FullText.java - created on May 17, 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

/**
 * This entry represents an arbitrary content object.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 17, 2013
 */
public class Content {
    /**
     * specifies what the type the content object is
     */
    private String type;

    /**
     * content object as a byte array
     */
    private byte[] object;

    /**
     * Creates a new instance of this class.
     */
    public Content() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param type
     *            specifies what the type the content object is
     * @param object
     *            content object as a byte array
     */
    public Content(String type, byte[] object) {
        this.type = type;
        this.object = object;
    }

    /**
     * @return specifies what the type the content object is
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            specifies what the type the content object is
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return content object as a byte array
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object
     *            content object as a byte array
     */
    public void setObject(byte[] object) {
        this.object = object;
    }
}
