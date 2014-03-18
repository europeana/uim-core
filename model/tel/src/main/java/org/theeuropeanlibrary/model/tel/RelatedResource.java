/* RelatedResource.java - created on 5 de Abr de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * Reference to another resource (somehow related with this one) that may exist in TEL or not
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 5 de Abr de 2013
 */
public class RelatedResource {
    /**
     * A brief text (typically a title, a names or one short sentence) describing the resource
     */
    @FieldId(1)
    private String           description;

    /**
     * Identifiers of the resource in external data sets
     */
    @FieldId(2)
    private List<Identifier> identifiers;

    /**
     * Identifier of the resource in the TEL repository
     */
    @FieldId(3)
    private Long             telIdentifier;

    /**
     * Creates a new instance of this class.
     */
    public RelatedResource() {
        this.identifiers = new ArrayList<Identifier>(0);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param description
     * @param identifiers
     */
    public RelatedResource(String description, List<Identifier> identifiers) {
        super();
        this.description = description;
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>(0);
        } else {
            this.identifiers = identifiers;
        }
    }

    /**
     * @return A brief text (typically a title, a names or one short sentence) describing the
     *         resource
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param description
     *            A brief text (typically a title, a names or one short sentence) describing the
     *            resource
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Identifiers of the resource in external data sets
     */
    public final List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param identifiers
     *            Identifiers of the resource in external data sets
     */
    public final void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * @return Identifier of the resource in the TEL repository
     */
    public final Long getTelIdentifier() {
        return telIdentifier;
    }

    /**
     * @param telIdentifier
     *            Identifier of the resource in the TEL repository
     */
    public final void setTelIdentifier(Long telIdentifier) {
        this.telIdentifier = telIdentifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result + ((telIdentifier == null) ? 0 : telIdentifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RelatedResource other = (RelatedResource)obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!identifiers.equals(other.identifiers)) return false;
        if (telIdentifier == null) {
            if (other.telIdentifier != null) return false;
        } else if (!telIdentifier.equals(other.telIdentifier)) return false;
        return true;
    }
}
