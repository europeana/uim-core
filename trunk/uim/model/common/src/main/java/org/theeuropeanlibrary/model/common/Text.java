/* Text.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;


/**
 * Any textual content, or textual description of a resource
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 21, 2011
 */
public class Text {
    /**
     * textual content
     */
    @FieldId(1)
    private String content;

    /**
     * Creates a new instance of this class.
     */
    public Text() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param textualContent
     *            textual content
     */
    public Text(String textualContent) {
        if (textualContent == null) { throw new IllegalArgumentException(
                "Argument 'textualContent' should not be null!"); }
        this.content = textualContent;
    }

    /**
     * @return textual content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            textual content
     */
    public void setContent(String content) {
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
        Text other = (Text)obj;
        if (content == null) {
            if (other.content != null) return false;
        } else if (!content.equals(other.content)) return false;
        return true;
    }
    
    
}
