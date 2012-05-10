/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.spatial;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * Class for places referenced by name
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 21, 2011
 */
public class NamedPlace extends SpatialEntity {
    /**
     * Name of this place like France, Berlin etc.
     */
    @FieldId(2)
    private String placeName;

    /**
     * Creates a new instance of this class.
     */
    public NamedPlace() {
        super(new ArrayList<Identifier>());
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     * @param identifiers
     *            Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    public NamedPlace(String placeName, List<Identifier> identifiers) {
        super(identifiers);
        this.placeName = placeName;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     */
    public NamedPlace(String placeName) {
        this();
        this.placeName = placeName;
    }

    /**
     * @return Name of this place like France, Berlin etc.
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            Name of this place like France, Berlin etc.
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((placeName == null) ? 0 : placeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        NamedPlace other = (NamedPlace)obj;
        if (placeName == null) {
            if (other.placeName != null) return false;
        } else if (!placeName.equals(other.placeName)) return false;
        return true;
    }

    @Override
    public String toString() {
        return placeName;
    }
    

    /**
     * @return a String readable by a human 
     */
    @Override
    public String getDisplay() {
        return placeName;
    }
}
