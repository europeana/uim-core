/* Edition.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.cluster;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * The edition of a resource, represented as a number and/or as a textual statement (revised
 * edition, commented edition, etc)
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Hash {
    /**
     * hash code
     */
    @FieldId(1)
    private String code;

    /**
     * Creates a new instance of this class.
     */
    public Hash() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param code
     *            hash code
     */
    public Hash(String code) {
        if (code == null) { throw new IllegalArgumentException(
                "Argument 'code' should not be null!"); }
        this.code = code;
    }

    /**
     * @return hash code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            hash code
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Hash other = (Hash)obj;
        if (code == null) {
            if (other.code != null) return false;
        } else if (!code.equals(other.code)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Hash [code=" + code + "]";
    }
}
