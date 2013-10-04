/* AccessPermissions.java - created on 11 de Set de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Allow TEL to tag records for inclusion or exclusion in certain scenarios of sharing the record,
 * such as Europeana, LOD, etc.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 11 de Set de 2013
 */
public class AccessPermission {
    /**
     * Access statement
     * 
     * @author Nuno Freire (nfreire@gmail.com)
     * @since 19/09/2013
     */
    public enum Access {
        /** Access DENY */
        DENY,
        /** Access ALLOW */
        ALLOW
    }

    /**
     * a tag identifying a context where access to this record is being specified
     */
    @FieldId(1)
    protected String context;

    /**
     * What kind of access permission should be applied in the context
     */
    @FieldId(2)
    protected Access access;

    /**
     * Creates a new instance of this class.
     */
    public AccessPermission() {
        // nothing to do
    }

    /**
     * Returns the context.
     * 
     * @return the context
     */
    public final String getContext() {
        return context;
    }

    /**
     * Sets the context
     * 
     * @param context
     *            the context to set
     */
    public final void setContext(String context) {
        this.context = context;
    }

    /**
     * Returns the access.
     * 
     * @return the access
     */
    public final Access getAccess() {
        return access;
    }

    /**
     * Sets the access
     * 
     * @param access
     *            the access to set
     */
    public final void setAccess(Access access) {
        this.access = access;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((access == null) ? 0 : access.hashCode());
        result = prime * result + ((context == null) ? 0 : context.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AccessPermission other = (AccessPermission)obj;
        if (access != other.access) return false;
        if (context == null) {
            if (other.context != null) return false;
        } else if (!context.equals(other.context)) return false;
        return true;
    }
}
