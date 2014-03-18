/* HistoricalPeriod.java - created on 22 de Mar de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;

/**
 * An historical period, characterized by a name and a geographic and temporal scope
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 22 de Mar de 2011
 */
public class HistoricalPeriod extends Temporal {
    /**
     * The name of the historical period
     */
    @FieldId(1)
    private String           name;
    /**
     * The temporal scope of the period
     */
    @FieldId(2)
    private Temporal         temporalScope;
    /**
     * The geographic scope of the period
     */
    @FieldId(3)
    private NamedPlace       geographicScope;

    /**
     * Identifiers of the historical period in external data sets
     */
    @FieldId(4)
    private List<Identifier> identifiers;

    /**
     * Creates a new instance of this class.
     * 
     * @param name
     *            The name of the historical period
     * @param temporalScope
     *            The temporal scope of the period
     * @param geographicScope
     *            The geographic scope of the period
     * @param identifiers
     *            Identifiers of the historical period in external data sets
     */
    public HistoricalPeriod(String name, Temporal temporalScope, NamedPlace geographicScope,
                            List<Identifier> identifiers) {
        super();
        if (name == null) { throw new IllegalArgumentException(
                "Argument 'name' should not be null!"); }
        if (temporalScope == null) { throw new IllegalArgumentException(
                "Argument 'temporalScope' should not be null!"); }
        if (geographicScope == null) { throw new IllegalArgumentException(
                "Argument 'geographicScope' should not be null!"); }
        this.name = name;
        this.temporalScope = temporalScope;
        this.geographicScope = geographicScope;
        if(identifiers==null)
            this.identifiers = new ArrayList<Identifier>(3);
        else
            this.identifiers = identifiers;
    }
    
    /**
     * Creates a new instance of this class.
     * 
     * @param name
     *            The name of the historical period
     * @param temporalScope
     *            The temporal scope of the period
     * @param geographicScope
     *            The geographic scope of the period
     * @param identifiers
     *            Identifiers of the historical period in external data sets
     */
    public HistoricalPeriod(String name, Temporal temporalScope, NamedPlace geographicScope,
                            Identifier... identifiers) {
        this(name, temporalScope, geographicScope, Arrays.asList(identifiers));
    }

    /**
     * Creates a new instance of this class.
     */
    public HistoricalPeriod() {
        identifiers = new ArrayList<Identifier>(3);
    }

    /**
     * @return The name of the historical period
     */
    public String getName() {
        return name;
    }

    /**
     * @return The temporal scope of the period
     */
    public Temporal getTemporalScope() {
        return temporalScope;
    }

    /**
     * @return The geographic scope of the period
     */
    public NamedPlace getGeographicScope() {
        return geographicScope;
    }

    /**
     * @return Identifiers of these time periods in external data sets
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param name
     *            The name of the historical period
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param temporalScope
     *            The temporal scope of the period
     */
    public void setTemporalScope(Temporal temporalScope) {
        this.temporalScope = temporalScope;
    }

    /**
     * @param geographicScope
     *            The geographic scope of the period
     */
    public void setGeographicScope(NamedPlace geographicScope) {
        this.geographicScope = geographicScope;
    }

    /**
     * @param identifiers
     *            Identifiers of these time periods in external data sets
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }
    
    @Override
    public String getDisplay() {
        String placeName=name;
        if (temporalScope != null) {
            placeName += (" (" + temporalScope.getDisplay() +")");
        }
        if (geographicScope != null) {
            placeName += (" (" + geographicScope.getDisplay() +")");
        }
        if(subject!=null) {
            String subjectHeadingString = subject.getSubjectHeadingDisplay();
            if(!subjectHeadingString.isEmpty())
                placeName=placeName+subjectHeadingString;
        }
        return placeName;
    }

    /**
     * @return a String readable by a human according to subject heading rules
     */
    public String getSubjectHeadingDisplay() {
        String placeName=name;
        if (temporalScope != null) {
            placeName += (" (" + temporalScope.getDisplay() +")");
        }
        if (geographicScope != null) {
            placeName += (" (" + geographicScope.getDisplay() +")");
        }
        if(subject!=null) {
            String subjectHeadingString = subject.getSubjectHeadingDisplay();
            if(!subjectHeadingString.isEmpty())
                return placeName+subjectHeadingString;
            else
                return placeName;
        }else
            return placeName;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geographicScope == null) ? 0 : geographicScope.hashCode());
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((temporalScope == null) ? 0 : temporalScope.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HistoricalPeriod other = (HistoricalPeriod)obj;
        if (geographicScope == null) {
            if (other.geographicScope != null) return false;
        } else if (!geographicScope.equals(other.geographicScope)) return false;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!CollectionUtils.isEqualCollection(identifiers,other.identifiers)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (temporalScope == null) {
            if (other.temporalScope != null) return false;
        } else if (!temporalScope.equals(other.temporalScope)) return false;
        return true;
    }
    
    
    
}
