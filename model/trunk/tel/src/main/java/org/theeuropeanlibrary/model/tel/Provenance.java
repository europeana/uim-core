/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;


/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Nov 15, 2012
 */
public class Provenance {
    /**
     * the controlled value
     * */
    @FieldId(1)
    private String collectionId;

    /**
     * An identification of the vocabulary according to which the value is encoded
     */
    @FieldId(2)
    private String providerId;

    /**
     * Creates a new instance of this class.
     */
    public Provenance() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * @param providerid 
     * @param collectionid 
     * 
     */
    public Provenance(String providerid, String collectionid) {
        this.providerId = providerid;
        this.collectionId = collectionid;
    }

    /**
     * Returns the collectionId.
     * @return the collectionId
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * Sets the collectionId to the given value.
     * @param collectionId the collectionId to set
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * Returns the providerId.
     * @return the providerId
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * Sets the providerId to the given value.
     * @param providerId the providerId to set
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collectionId == null) ? 0 : collectionId.hashCode());
        result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Provenance other = (Provenance)obj;
        if (collectionId == null) {
            if (other.collectionId != null) return false;
        } else if (!collectionId.equals(other.collectionId)) return false;
        if (providerId == null) {
            if (other.providerId != null) return false;
        } else if (!providerId.equals(other.providerId)) return false;
        return true;
    }
    
}
