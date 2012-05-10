/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;


/**
 * A value created according to a controlled vocabulary. Meant to be used for the creation of
 * facets, for example.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 21, 2011
 */
public class Facet {
    /**
     * the controlled value
     * */
    @FieldId(1)
    private String value;

    /**
     * An identification of the vocabulary according to which the value is encoded
     */
    @FieldId(2)
    private String vocabulary;

    /**
     * Creates a new instance of this class.
     */
    public Facet() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param value
     *            the controlled value
     */
    public Facet(String value) {
        this(value, null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param value
     *            the controlled value
     * @param vocabulary
     *            An identification of the vocabulary according to which the value is encoded
     */
    public Facet(String value, String vocabulary) {
        super();
        if (value == null) { throw new IllegalArgumentException(
                "Argument 'value' should not be null!"); }
        this.value = value;
        this.vocabulary = vocabulary;
    }

    /**
     * @return the controlled value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return An identification of the vocabulary according to which the value is encoded
     */
    public String getVocabulary() {
        return vocabulary;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param vocabulary
     */
    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((vocabulary == null) ? 0 : vocabulary.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Facet other = (Facet)obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        if (vocabulary == null) {
            if (other.vocabulary != null) return false;
        } else if (!vocabulary.equals(other.vocabulary)) return false;
        return true;
    }
    
    
}
