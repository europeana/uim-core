/* FulltextBlock.java - created on Jul 22, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides metadata to specify a block in a fulltext object that represents an article
 * for example.
 * 
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Jul 22, 2014
 */
public class FullTextBlock {
    @FieldId(1)
    private String                  caption;

    @FieldId(2)
    private String                  type;

    @FieldId(3)
    private List<FullTextBlockPart> parts;

    /**
     * Creates a new instance of this class.
     */
    public FullTextBlock() {
        super();
        this.caption = "";
        this.type = "text";
        this.parts = new ArrayList<FullTextBlockPart>();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param caption
     * @param type
     */
    public FullTextBlock(String caption, String type) {
        this.caption = caption;
        this.type = type;
        this.parts = new ArrayList<FullTextBlockPart>();
    }

    /**
     * Returns the caption.
     * 
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption to the given value.
     * 
     * @param caption
     *            the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Returns the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type to the given value.
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the parts.
     * 
     * @return the parts
     */
    public List<FullTextBlockPart> getParts() {
        return parts;
    }

    /**
     * Sets the parts to the given value.
     * 
     * @param parts
     *            the parts to set
     */
    public void setParts(List<FullTextBlockPart> parts) {
        this.parts = parts;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caption == null) ? 0 : caption.hashCode());
        result = prime * result + ((parts == null) ? 0 : parts.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FullTextBlock other = (FullTextBlock)obj;
        if (caption == null) {
            if (other.caption != null) return false;
        } else if (!caption.equals(other.caption)) return false;
        if (parts == null) {
            if (other.parts != null) return false;
        } else if (!parts.equals(other.parts)) return false;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "FullTextBlock [caption=" + caption + ", type=" + type + ", parts=" + parts + "]";
    }
}
