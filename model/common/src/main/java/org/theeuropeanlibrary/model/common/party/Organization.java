/* Person.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.party;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * Class represents a group.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Organization extends Party {
    /**
     * an organizational sub division
     */
    @FieldId(3)
    protected String subdivision;

    /**
     * Creates a new instance of this class.
     */
    public Organization() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupName
     *            name of the organization
     */
    public Organization(String groupName) {
        super(groupName);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupName
     *            name of the organization
     * @param subdivision
     *            name of an organizational unit within the organization
     */
    public Organization(String groupName, String subdivision) {
        super(groupName);
        this.subdivision = subdivision;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupName
     *            name of the organization
     * @param identifiers
     */
    public Organization(String groupName, List<Identifier> identifiers) {
        super(groupName, identifiers);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param groupName
     *            name of the organization
     * @param subdivision
     * @param identifiers
     */
    public Organization(String groupName, String subdivision, List<Identifier> identifiers) {
        super(groupName, identifiers);
        this.subdivision = subdivision;
    }

    /**
     * @return subdivision
     */
    public String getSubdivision() {
        return subdivision;
    }

    /**
     * @param subdivision
     */
    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    /**
     * @return a string representation of the party for end user display
     */
    @Override
    public String getDisplay() {
        String ret=partyName;
        if (subdivision != null && !subdivision.trim().isEmpty()) 
            ret += StringUtils.stripEnd(ret, ".,;:?") + ". " + subdivision;
        if (location!=null) 
            ret += "(" + location.getDisplay() + ")";
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + super.hashCode();
        result = prime * result + ((subdivision == null) ? 0 : subdivision.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Organization other = (Organization)obj;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!CollectionUtils.isEqualCollection(identifiers, other.identifiers))
            return false;
        if (partyName == null) {
            if (other.partyName != null) return false;
        } else if (!partyName.equals(other.partyName)) return false;
        if (subdivision == null) {
            if (other.subdivision != null) return false;
        } else if (!subdivision.equals(other.subdivision)) return false;
        return true;
    }
}
