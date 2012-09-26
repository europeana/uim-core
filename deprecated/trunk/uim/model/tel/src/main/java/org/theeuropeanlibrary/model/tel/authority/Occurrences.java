/* StringOccurrences.java - created on 12 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.authority;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Used in authority records to hold title, coauthor and publishers associated with an authority
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @param <T>
 *            any type to count the occurrences
 * @date 12 de Jul de 2011
 */
public class Occurrences<T> {
    /**
     * A value (title, or name)
     */
    @FieldId(1)
    private T   value;
    /**
     * the number of occurrences of this data associated with the authority
     */
    @FieldId(2)
    private int count;

    /**
     * Creates a new instance of this class. protected so that it can only be used by repository
     * converters
     */
    public Occurrences() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param value
     *            A string value (title, or name)
     * @param occurences
     *            the number of occurrences of this data associated with the authority
     */
    public Occurrences(T value, int occurences) {
        super();
        this.value = value;
        this.count = occurences;
    }

    /**
     * @return A value (title, or name)
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value
     *            A value (title, or name)
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the number of occurrences of this data associated with the authority
     */
    public int getCount() {
        return count;
    }

    /**
     * @param occurences
     *            the number of occurrences of this data associated with the authority
     */
    public void setCount(int occurences) {
        this.count = occurences;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Occurrences<?> other = (Occurrences<?>)obj;
        if (count != other.count) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public String toString() {
        if (value != null) return value.toString() + "(" + count + ")";
        return "";
    }
}
