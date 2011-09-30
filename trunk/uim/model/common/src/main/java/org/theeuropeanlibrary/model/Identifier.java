/* Identifier.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Base class for identifiers.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 30, 2011
 */
public class Identifier {
    /**
     * actual value of identifier
     */
    @FieldId(1)
    private String identifier;

    /**
     * scope of identifier
     */
    @FieldId(2)
    private String scope;

    /**
     * Creates a new instance of this class.
     */
    public Identifier() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param identifier
     *            actual value of identifier
     */
    public Identifier(String identifier) {
        this(identifier, null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param identifier
     *            actual value of identifier
     * @param scope
     *            scope of id
     */
    public Identifier(String identifier, String scope) {
        this.identifier = identifier;
        this.scope = scope;
    }

    /**
     * @return actual value of identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier
     *            actual value of identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return scope of identifier
     */
    public String getScope() {
        return scope;
    }

    /**
     * @param scope
     *            scope of identifier
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Identifier other = (Identifier)obj;
        if (identifier == null) {
            if (other.identifier != null) return false;
        } else if (!identifier.equals(other.identifier)) return false;
        if (scope == null) {
            if (other.scope != null) return false;
        } else if (!scope.equals(other.scope)) return false;
        return true;
    }
    
    @Override
    public String toString() {
    	return getStringEncodedForm();
    }
    
	/**
	 * Encodes the Identifier encoded in a String, in the form <scope>:<identifier>
	 * 
	 * @return encoded id string
	 */
	public String getStringEncodedForm() {
		if(getScope()!=null)
			try {
				return URLEncoder.encode(getScope(), "UTF-8")+":"+getIdentifier();
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 not supported???", e);
			}
		return ":"+getIdentifier();
	}
	
	/**
	 * Creates an Identifier from an encoded form (as the result of the getStringEncodedForm method)
	 * 
	 * @param encodedIdString in the form <scope>:<identifier>
	 * @return identifier
	 */
	public static Identifier decodeIdentifier(String encodedIdString) {
		try {
			int idxColon=encodedIdString.indexOf(':');
			if(idxColon==-1)
				throw new IllegalArgumentException("Invalid encoded ID (no colon found): "+encodedIdString);
			Identifier id=null;
			if(idxColon==0) {
				id=new Identifier(encodedIdString.substring(1));
			}else {
				String idPart = encodedIdString.substring(idxColon+1);
				String scopePart = URLDecoder.decode(encodedIdString.substring(0, idxColon), "UTF-8");
				id=new Identifier(idPart, scopePart);
			}
			return id;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported???", e);
		}	
	}
}
