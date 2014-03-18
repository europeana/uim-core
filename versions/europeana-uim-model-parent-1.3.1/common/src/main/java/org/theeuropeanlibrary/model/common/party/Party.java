/* Party.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.party;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.subject.Subject;

/**
 * Defining a party like company or a person. Those entities share a name throughout all of them.
 * This class may be used when the specific type of party is unknown.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Party {
    /**
     * name of this party like person etc.
     */
    @FieldId(1)
    protected String           partyName;

    /**
     * Identifiers of the party
     */
    @FieldId(2)
    protected List<Identifier> identifiers;

    /**
     * If this party is in the role of a subject additional informations can be retrieved through
     * this subject object.
     */
    @FieldId(9)
    protected Subject          subject;

    /**
     * Where the party is located, or the geographical scope of its operation.
     */
    @FieldId(10)
    protected SpatialEntity    location;

    /**
     * Creates a new instance of this class.
     */
    public Party() {
        identifiers = new ArrayList<Identifier>();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param partyName
     *            name of this party like person etc.
     */
    public Party(String partyName) {
        this();
        if (partyName == null) { throw new IllegalArgumentException(
                "Argument 'partyName' should not be null!"); }
        this.partyName = partyName;
        identifiers = new ArrayList<Identifier>();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param partyName
     *            name of this party like person etc.
     * @param identifiers
     *            Identifiers of the party
     */
    public Party(String partyName, List<Identifier> identifiers) {
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>();
        } else {
            this.identifiers = identifiers;
        }
        this.partyName = partyName;
    }

    /**
     * @return name of this party like person etc.
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * @return Identifiers of the party
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param partyName
     *            name of this party like person etc.
     */
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * @param identifiers
     *            Identifiers of the party
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * @return a string representation of the party for end user display
     */
    public String getDisplay() {
        return partyName;
    }

    /**
     * @return If this party is in the role of a subject additional informations can be retrieved
     *         throught this subject object.
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            If this party is in the role of a subject additional informations can be retrieved
     *            throught this subject object.
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Returns the location.
     * 
     * @return Where the party is located, or the geographical scope of its operation.
     */
    public SpatialEntity getLocation() {
        return location;
    }

    /**
     * Sets the location to the given value.
     * 
     * @param location
     *            Where the party is located, or the geographical scope of its operation.
     */
    public void setLocation(SpatialEntity location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result + ((partyName == null) ? 0 : partyName.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Party other = (Party)obj;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!identifiers.equals(other.identifiers)) return false;
        if (partyName == null) {
            if (other.partyName != null) return false;
        } else if (!partyName.equals(other.partyName)) return false;
        if (subject == null) {
            if (other.subject != null) return false;
        } else if (!subject.equals(other.subject)) return false;
        if (location == null) {
            if (other.location != null) return false;
        } else if (!location.equals(other.location)) return false;
        return true;
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
