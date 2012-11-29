/* Person.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.spatial;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * Class extending place to hold a geographic bounding box. Inspired by dublin core dcmi-box
 * (http://dublincore.org/documents/dcmi-box/)
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 30, 2011
 */
public class BoundingBoxReferencedPlace extends NamedPlace {
    /**
     * The north limit in decimal degrees (WGS 84)
     */
    @FieldId(3)
    private Double northLimit;

    /**
     * The south limit in decimal degrees (WGS 84)
     */
    @FieldId(4)
    private Double southLimit;

    /**
     * The east limit in decimal degrees (WGS 84)
     */
    @FieldId(5)
    private Double eastLimit;

    /**
     * The west limit in decimal degrees (WGS 84)
     */
    @FieldId(6)
    private Double westLimit;

    /**
     * The name of the projection used with any parameters required, such as ellipsoid parameters,
     * datum, standard parallels and meridians, zone, etc
     */
    @FieldId(7)
    private String projection;

    /** 
     * The constant coordinate for the uppermost face or edge (in meters)
     */
    @FieldId(9)
    private Double upLimit;
    
    /** 
     * The constant coordinate for the lowermost face or edge (in meters)
     */
    @FieldId(10)
    private Double downLimit;

    
    /**
     * Creates a new instance of this class.
     */
    public BoundingBoxReferencedPlace() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     * @param identifiers
     *            Identifiers of the geographic entity in external data sets (Geonames, etc)
     * @param northLimit
     *            The north limit in decimal degrees (WGS 84)
     * @param southLimit
     *            The south limit in decimal degrees (WGS 84)
     * @param eastLimit
     *            The east limit in decimal degrees (WGS 84)
     * @param westLimit
     *            The west limit in decimal degrees (WGS 84)
     * @param upLimit The constant coordinate for the uppermost face or edge (in meters)
     * @param downLimit The constant coordinate for the lowermost face or edge (in meters)
     */
    public BoundingBoxReferencedPlace(String placeName, List<Identifier> identifiers,
            Double northLimit, Double southLimit, Double eastLimit,
            Double westLimit, Double upLimit, Double downLimit) {
        super(placeName, identifiers);
        this.northLimit = northLimit;
        this.southLimit = southLimit;
        this.eastLimit = eastLimit;
        this.westLimit = westLimit;
        this.upLimit = upLimit;
        this.downLimit = downLimit;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     * @param northLimit
     *            The north limit in decimal degrees (WGS 84)
     * @param southLimit
     *            The south limit in decimal degrees (WGS 84)
     * @param eastLimit
     *            The east limit in decimal degrees (WGS 84)
     * @param westLimit
     *            The west limit in decimal degrees (WGS 84)
     * @param upLimit The constant coordinate for the uppermost face or edge (in meters)
     * @param downLimit The constant coordinate for the lowermost face or edge (in meters)
     */
    public BoundingBoxReferencedPlace(String placeName, Double northLimit, Double southLimit,
            Double eastLimit, Double westLimit, Double upLimit, Double downLimit) {
        super(placeName, new ArrayList<Identifier>());
        this.northLimit = northLimit;
        this.southLimit = southLimit;
        this.eastLimit = eastLimit;
        this.westLimit = westLimit;
        this.upLimit = upLimit;
        this.downLimit = downLimit;
    }

    /**
     * @return The north limit in decimal degrees (WGS 84)
     */
    public Double getNorthLimit() {
        return northLimit;
    }

    /**
     * @param northLimit
     *            The north limit in decimal degrees (WGS 84)
     */
    public void setNorthLimit(Double northLimit) {
        this.northLimit = northLimit;
    }

    /**
     * @return The south limit in decimal degrees (WGS 84)
     */
    public Double getSouthLimit() {
        return southLimit;
    }

    /**
     * @param southLimit
     *            The south limit in decimal degrees (WGS 84)
     */
    public void setSouthLimit(Double southLimit) {
        this.southLimit = southLimit;
    }

    /**
     * @return The east limit in decimal degrees (WGS 84)
     */
    public Double getEastLimit() {
        return eastLimit;
    }

    /**
     * @param eastLimit
     *            The east limit in decimal degrees (WGS 84)
     */
    public void setEastLimit(Double eastLimit) {
        this.eastLimit = eastLimit;
    }

    /**
     * @return The west limit in decimal degrees (WGS 84)
     */
    public Double getWestLimit() {
        return westLimit;
    }

    /**
     * @param westLimit
     *            The west limit in decimal degrees (WGS 84)
     */
    public void setWestLimit(Double westLimit) {
        this.westLimit = westLimit;
    }

    /**
     * @return The name of the projection used with any parameters required, such as ellipsoid
     *         parameters, datum, standard parallels and meridians, zone, etc
     */
    public String getProjection() {
        return projection;
    }

    /**
     * @param projection
     *            The name of the projection used with any parameters required, such as ellipsoid
     *            parameters, datum, standard parallels and meridians, zone, etc
     */
    public void setProjection(String projection) {
        this.projection = projection;
    }

   
    
    /**
     * @return a String readable by a human 
     */
    @Override
    public String getDisplay() {
        String box="";
        if(westLimit!=null) {
            box += convertDecimalDegreesToDms(westLimit, true);
        }
        if(eastLimit!=null) {
            if(!box.isEmpty())
                box+=" - ";
            box += convertDecimalDegreesToDms(eastLimit, true);
        }
        if(!box.isEmpty() && (northLimit!=null || southLimit!=null))
            box+=" / ";
        if(northLimit!=null) {
            box += convertDecimalDegreesToDms(northLimit, false);
        }
        if(southLimit!=null) {
            if(!box.isEmpty())
                box+=" - ";
            box += convertDecimalDegreesToDms(southLimit, false);
        }
        
        if(downLimit!=null) {
            if(upLimit!=null) {
                box=String.format("%s ; %1.0f-%1.0f m", box, downLimit, upLimit);
            }else
                box=String.format("%s ; %1.0f m", box, downLimit);
        }else if(upLimit!=null) 
            box=String.format("%s ; %1.0f m", box, upLimit);
        
        if(projection!=null) {
            box=box+" ; "+ projection;            
        }

        if(getPlaceName()!=null)
            return getPlaceName()+ " ; " + box;
        return box;
    }

    /**
     * Sets the upLimit
     * @param upLimit the upLimit to set
     */
    public void setUpLimit(Double upLimit) {
        this.upLimit = upLimit;
    }

    /**
     * Returns the upLimit.
     * @return the upLimit
     */
    public Double getUpLimit() {
        return upLimit;
    }

    /**
     * Sets the downLimit
     * @param downLimit the downLimit to set
     */
    public void setDownLimit(Double downLimit) {
        this.downLimit = downLimit;
    }

    /**
     * Returns the downLimit.
     * @return the downLimit
     */
    public Double getDownLimit() {
        return downLimit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((downLimit == null) ? 0 : downLimit.hashCode());
        result = prime * result + ((eastLimit == null) ? 0 : eastLimit.hashCode());
        result = prime * result + ((northLimit == null) ? 0 : northLimit.hashCode());
        result = prime * result + ((projection == null) ? 0 : projection.hashCode());
        result = prime * result + ((southLimit == null) ? 0 : southLimit.hashCode());
        result = prime * result + ((upLimit == null) ? 0 : upLimit.hashCode());
        result = prime * result + ((westLimit == null) ? 0 : westLimit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        BoundingBoxReferencedPlace other = (BoundingBoxReferencedPlace)obj;
        if (downLimit == null) {
            if (other.downLimit != null) return false;
        } else if (!downLimit.equals(other.downLimit)) return false;
        if (eastLimit == null) {
            if (other.eastLimit != null) return false;
        } else if (!eastLimit.equals(other.eastLimit)) return false;
        if (northLimit == null) {
            if (other.northLimit != null) return false;
        } else if (!northLimit.equals(other.northLimit)) return false;
        if (projection == null) {
            if (other.projection != null) return false;
        } else if (!projection.equals(other.projection)) return false;
        if (southLimit == null) {
            if (other.southLimit != null) return false;
        } else if (!southLimit.equals(other.southLimit)) return false;
        if (upLimit == null) {
            if (other.upLimit != null) return false;
        } else if (!upLimit.equals(other.upLimit)) return false;
        if (westLimit == null) {
            if (other.westLimit != null) return false;
        } else if (!westLimit.equals(other.westLimit)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "BoundingBoxReferencedPlace [northLimit=" + northLimit + ", southLimit=" +
               southLimit + ", eastLimit=" + eastLimit + ", westLimit=" + westLimit +
               ", projection=" + projection + ", upLimit=" + upLimit + ", downLimit=" + downLimit +
               "]";
    }
    
    
}


