/* StructuralRelationType.java - created on 13/12/2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * A enum to represent sequences and hierarquies within the object model, using relations inside the MetadataRecord
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 13/12/2013
 */
public enum StructuralRelationType {
    /** StructuralRelationType PREVIOUS_IN_SEQUENCE */
    PREVIOUS_IN_SEQUENCE, 
    /** StructuralRelationType NEXT_IN_SEQUENCE */
    NEXT_IN_SEQUENCE, 
    /** StructuralRelationType PARENT */
    PARENT, 
    /** StructuralRelationType CHILD */
    CHILD; 
}
