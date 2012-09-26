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
    private double northLimit;

    /**
     * The south limit in decimal degrees (WGS 84)
     */
    @FieldId(4)
    private double southLimit;

    /**
     * The east limit in decimal degrees (WGS 84)
     */
    @FieldId(5)
    private double eastLimit;

    /**
     * The west limit in decimal degrees (WGS 84)
     */
    @FieldId(6)
    private double westLimit;

    /**
     * The name of the projection used with any parameters required, such as ellipsoid parameters,
     * datum, standard parallels and meridians, zone, etc
     */
    @FieldId(7)
    private String projection;

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
     */
    public BoundingBoxReferencedPlace(String placeName, List<Identifier> identifiers,
                                      double northLimit, double southLimit, double eastLimit,
                                      double westLimit) {
        super(placeName, identifiers);
        this.northLimit = northLimit;
        this.southLimit = southLimit;
        this.eastLimit = eastLimit;
        this.westLimit = westLimit;
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
     */
    public BoundingBoxReferencedPlace(String placeName, double northLimit, double southLimit,
                                      double eastLimit, double westLimit) {
        super(placeName, new ArrayList<Identifier>());
        this.northLimit = northLimit;
        this.southLimit = southLimit;
        this.eastLimit = eastLimit;
        this.westLimit = westLimit;
    }

    /**
     * @return The north limit in decimal degrees (WGS 84)
     */
    public double getNorthLimit() {
        return northLimit;
    }

    /**
     * @param northLimit
     *            The north limit in decimal degrees (WGS 84)
     */
    public void setNorthLimit(double northLimit) {
        this.northLimit = northLimit;
    }

    /**
     * @return The south limit in decimal degrees (WGS 84)
     */
    public double getSouthLimit() {
        return southLimit;
    }

    /**
     * @param southLimit
     *            The south limit in decimal degrees (WGS 84)
     */
    public void setSouthLimit(double southLimit) {
        this.southLimit = southLimit;
    }

    /**
     * @return The east limit in decimal degrees (WGS 84)
     */
    public double getEastLimit() {
        return eastLimit;
    }

    /**
     * @param eastLimit
     *            The east limit in decimal degrees (WGS 84)
     */
    public void setEastLimit(double eastLimit) {
        this.eastLimit = eastLimit;
    }

    /**
     * @return The west limit in decimal degrees (WGS 84)
     */
    public double getWestLimit() {
        return westLimit;
    }

    /**
     * @param westLimit
     *            The west limit in decimal degrees (WGS 84)
     */
    public void setWestLimit(double westLimit) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(eastLimit);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(northLimit);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        result = prime * result + ((projection == null) ? 0 : projection.hashCode());
        temp = Double.doubleToLongBits(southLimit);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(westLimit);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        BoundingBoxReferencedPlace other = (BoundingBoxReferencedPlace)obj;
        if (Double.doubleToLongBits(eastLimit) != Double.doubleToLongBits(other.eastLimit))
            return false;
        if (Double.doubleToLongBits(northLimit) != Double.doubleToLongBits(other.northLimit))
            return false;
        if (projection == null) {
            if (other.projection != null) return false;
        } else if (!projection.equals(other.projection)) return false;
        if (Double.doubleToLongBits(southLimit) != Double.doubleToLongBits(other.southLimit))
            return false;
        if (Double.doubleToLongBits(westLimit) != Double.doubleToLongBits(other.westLimit))
            return false;
        return true;
    }
    
    /**
     * @return a String readable by a human 
     */
    @Override
    public String getDisplay() {
        //TODO: use a friendly display for coordinates
        String box="northLimit:"+northLimit + "southLimit:"+southLimit + "eastLimit:"+eastLimit + "westLimit:"+westLimit;
        if (projection!=null && !projection.isEmpty())
            box += " projection:"+projection;
            
        if(getPlaceName()!=null)
            return getPlaceName()+ "("+box+")";
        return box;
    }
}
