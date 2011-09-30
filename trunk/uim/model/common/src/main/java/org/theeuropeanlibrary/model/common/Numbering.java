/* Numbering.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;


/**
 * Defining a numbering for volume, issue etc and scope of validity.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public class Numbering {
    /**
     * actual value of numbering
     */
    @FieldId(1)
    private int number;

    /**
     * Creates a new instance of this class.
     */
    public Numbering() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param number
     *            actual value of numbering
     */
    public Numbering(int number) {
        if (number < 0) { throw new IllegalArgumentException(
                "Argument 'number' should be greater zero!"); }
        this.number = number;
    }

    /**
     * @return actual value of numbering
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number
     *            actual value of numbering
     */
    public void setNumber(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Argument 'number' should be greater zero!");
        this.number = number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Numbering other = (Numbering)obj;
        if (number != other.number) return false;
        return true;
    }
    
    
}
