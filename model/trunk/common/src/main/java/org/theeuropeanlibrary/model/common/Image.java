/* Image.java - created on Jun 13, 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

/**
 * This class holds an image object!
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 13, 2013
 */
public class Image {
    /**
     * textual content
     */
    @FieldId(1)
    private byte[] content;

    /**
     * Creates a new instance of this class.
     */
    public Image() {
        // nothing to do, for serialization purpose
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param content
     *            content
     */
    public Image(byte[] content) {
        if (content == null) { throw new IllegalArgumentException(
                "Argument 'textualContent' should not be null!"); }
        this.content = content;
    }

    /**
     * @return content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(byte[] content) {
        if (content == null) { throw new IllegalArgumentException(
                "Argument 'content' should not be null!"); }
        this.content = content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Image other = (Image)obj;
        if (content == null) {
            if (other.content != null) return false;
        } else if (!content.equals(other.content)) return false;
        return true;
    }
}
