/* SpatialEntity.java - created on 22 de Mar de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.spatial;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.subject.Subject;

/**
 * Base class for all representations of Geographic concepts
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 22 de Mar de 2011
 */
public class SpatialEntity {
    /**
     * Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    @FieldId(1)
    private List<Identifier> identifiers;

    /**
     * If this spatial entity is in the role of a subject additional informations can be retrieved
     * throught this subject object.
     */
    @FieldId(8)
    protected Subject        subject;

    /**
     * Creates a new instance of this class.
     */
    public SpatialEntity() {
        this.identifiers = new ArrayList<Identifier>(0);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param identifiers
     *            Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    public SpatialEntity(List<Identifier> identifiers) {
        super();
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>(0);
        } else {
            this.identifiers = identifiers;
        }
    }

    /**
     * @return Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param identifiers
     *            Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * @return If this spatial entity is in the role of a subject additional informations can be
     *         retrieved throught this subject object.
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            If this spatial entity is in the role of a subject additional informations can be
     *            retrieved throught this subject object.
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    /**
     * @param coordInDecimalDegrees
     * @param isLongitude ture it is longitude, false it is latitude
     * @return coordinate in DMS - Degrees-Minutes-Seconds
     */
    protected String convertDecimalDegreesToDms(double coordInDecimalDegrees, boolean isLongitude) {
        char dir;
        if(isLongitude) {
            dir=coordInDecimalDegrees>=0 ? 'E' : 'W';
        }else {
            dir=coordInDecimalDegrees>=0 ? 'N' : 'S';
        }
        double coord=Math.abs(coordInDecimalDegrees);
        int deg=(int)Math.floor(coord);
        int min=(int)Math.floor((coord-deg) * 60);
        int sec=(int)Math.round(((coord-deg) * 60 - min) * 60);
        if(sec==0) {
            if(min==0) 
                return String.format("%s %dº", dir, deg);
            return String.format("%s %dº%d'", dir, deg, min);            
        }
        return String.format("%s %dº%d'%d\"", dir, deg, min, sec);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SpatialEntity other = (SpatialEntity)obj;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!identifiers.equals(other.identifiers)) return false;
        if (subject == null) {
            if (other.subject != null) return false;
        } else if (!subject.equals(other.subject)) return false;
        return true;
    }
    
    /**
     * @return a String readable by a human 
     */
    public String getDisplay() {
        if(identifiers!=null)
            for(Identifier id: identifiers)
                return id.getIdentifier();
        return "";
    }
}
