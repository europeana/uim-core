/* Edition.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;


/**
 * The edition of a resource, represented as a number and/or as a textual statement (revised
 * edition, commented edition, etc)
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Edition {
    /**
     * the edition number
     */
    @FieldId(1)
    private Integer number;
    /**
     * textual edition statement
     */
    @FieldId(2)
    private String  statement;

    /**
     * Creates a new instance of this class.
     */
    public Edition() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param number
     *            the edition number
     * @param statement
     *            textual edition statement
     */
    public Edition(Integer number, String statement) {
        this.number = number;
        this.statement = statement;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param statement
     *            textual edition statement
     */
    public Edition(String statement) {
        if (statement == null) { throw new IllegalArgumentException(
                "Argument 'statement' should not be null!"); }
        this.number = null;
        this.statement = statement;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param number
     *            the edition number
     */
    public Edition(Integer number) {
        if (number == null || number < 0) { throw new IllegalArgumentException(
                "Argument 'number' should be greater zero!"); }
        this.number = number;
        this.statement = null;
    }

    /**
     * @return the edition number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return textual edition statement
     */
    public String getStatement() {
        return statement;
    }

    /**
     * @param number
     *            the edition number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @param statement
     *            textual edition statement
     */
    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((statement == null) ? 0 : statement.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Edition other = (Edition)obj;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (statement == null) {
            if (other.statement != null) return false;
        } else if (!statement.equals(other.statement)) return false;
        return true;
    }
    
    
}
