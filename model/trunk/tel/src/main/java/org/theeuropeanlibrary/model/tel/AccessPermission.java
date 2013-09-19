/* AccessPermissions.java - created on 11 de Set de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;


import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Allow TEL to tag records for inclusion or exclusion in certain scenarios of sharing the record, such as Europeana, LOD, etc. 
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
    public enum Access{ 
    /** Access DENY */
    DENY, 
    /** Access ALLOW */
    ALLOW }
    
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
}
