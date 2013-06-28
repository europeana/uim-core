/* Redirect.java - created on Jun 28, 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.cluster;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * This class wraps a list of redirected IDs. In other words, this list contains all record IDs that
 * can be used instead of this record. It should be used for deleted records for which a replacement
 * exists under a different ID.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 28, 2013
 */
public class Redirect {
    /**
     * internal ID that is a replacement for a specific record
     */
    @FieldId(1)
    private Long redirectId;

    /**
     * Creates a new instance of this class.
     * 
     * @param redirectId
     *            internal ID that is a replacement for a specific record
     */
    public Redirect(Long redirectId) {
        this.redirectId = redirectId;
    }

    /**
     * @return internal ID that is a replacement for a specific record
     */
    public Long getRedirectId() {
        return redirectId;
    }

    /**
     * @param redirectId
     *            internal ID that is a replacement for a specific record
     */
    public void setRedirectId(Long redirectId) {
        this.redirectId = redirectId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((redirectId == null) ? 0 : redirectId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Redirect other = (Redirect)obj;
        if (redirectId == null) {
            if (other.redirectId != null) return false;
        } else if (!redirectId.equals(other.redirectId)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Redirect [redirectId=" + redirectId + "]";
    }
}
