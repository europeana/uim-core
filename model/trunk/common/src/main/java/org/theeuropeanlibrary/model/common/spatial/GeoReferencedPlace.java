/* Person.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.spatial;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * Class extending place to hold a geographic coordinates.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public class GeoReferencedPlace extends NamedPlace {
    /**
     * Latitude coordinate in decimal degrees (WGS 84)
     */
    @FieldId(3)
    private double latitude;

    /**
     * Longitude coordinate in decimal degrees (WGS 84)
     */
    @FieldId(4)
    private double longitude;

    /**
     * elevation in metres
     */
    @FieldId(9)
    private Double elevation;

    /**
     * Creates a new instance of this class.
     */
    public GeoReferencedPlace() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     * @param latitude
     *            Latitude coordinate in decimal degrees (WGS 84)
     * @param longitude
     *            Longitude coordinate in decimal degrees (WGS 84)
     * @param identifiers
     *            Identifiers of the geographic entity in external data sets (Geonames, etc)
     */
    public GeoReferencedPlace(String placeName, double latitude, double longitude,
                              List<Identifier> identifiers) {
        super(placeName, identifiers);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param placeName
     *            Name of this place like France, Berlin etc.
     * @param latitude
     *            Latitude coordinate in decimal degrees (WGS 84)
     * @param longitude
     *            Longitude coordinate in decimal degrees (WGS 84)
     */
    public GeoReferencedPlace(String placeName, double latitude, double longitude) {
        super(placeName, new ArrayList<Identifier>());
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return Latitude coordinate in decimal degrees (WGS 84)
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return Longitude coordinate in decimal degrees (WGS 84)
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param latitude
     *            Latitude coordinate in decimal degrees (WGS 84)
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude
     *            Longitude coordinate in decimal degrees (WGS 84)
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return a String readable by a human
     */
    @Override
    public String getDisplay() {
        String box = "";
        box += convertDecimalDegreesToDms(latitude, false);
        box += ", ";
        box += convertDecimalDegreesToDms(longitude, true);
        if (elevation != null) box = String.format("%s ; %1.0f m", box, elevation);

        if (getPlaceName() != null) return getPlaceName() + " ; ";
        return box;
    }

    /**
     * @return elevation
     */
    public final Double getElevation() {
        return elevation;
    }

    /**
     * @param elevation
     */
    public final void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((elevation == null) ? 0 : elevation.hashCode());
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        GeoReferencedPlace other = (GeoReferencedPlace)obj;
        if (elevation == null) {
            if (other.elevation != null) return false;
        } else if (!elevation.equals(other.elevation)) return false;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
            return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
            return false;
        return true;
    }

}
