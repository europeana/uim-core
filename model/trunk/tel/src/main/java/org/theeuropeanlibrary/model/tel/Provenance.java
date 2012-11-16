/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Holding information about collection and provider!
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Nov 15, 2012
 */
public class Provenance {
    /**
     * controller Id (mnemonic)
     * */
    @FieldId(1)
    private String collectionId;

    /**
     * provider Id (mnemonic)
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
     * 
     * @param providerid
     *            proider Id (mnemonic)
     * @param collectionid
     *            controller Id (mnemonic)
     * 
     */
    public Provenance(String providerid, String collectionid) {
        this.providerId = providerid;
        this.collectionId = collectionid;
    }

    /**
     * @return controller Id (mnemonic)
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * @param collectionId
     *            controller Id (mnemonic)
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * @return provider Id (mnemonic)
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId
     *            provider Id (mnemonic)
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

    @Override
    public String toString() {
        return "Provenance [collectionId=" + collectionId + ", providerId=" + providerId + "]";
    }
}
