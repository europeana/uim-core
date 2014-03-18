/* Edition.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.cluster;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Defining partitions in the object model.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Partition {
    /**
     * the partition number
     */
    @FieldId(1)
    private Integer number;

    /**
     * Creates a new instance of this class.
     */
    public Partition() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param number
     *            the partition number
     */
    public Partition(Integer number) {
        this.number = number;
    }

    /**
     * @return the partition number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number
     *            the partition number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Partition other = (Partition)obj;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Partition [number=" + number + "]";
    }
}
