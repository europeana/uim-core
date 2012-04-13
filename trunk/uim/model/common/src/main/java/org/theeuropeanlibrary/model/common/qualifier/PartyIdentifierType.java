/* PartyIdentifierType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * The type of identifiers for parties supported by TEL
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum PartyIdentifierType {
    /**
     * TEL assigned
     */
    TEL,
    /**
     * FIXME: certainty about enrichments should go in separate enum
     * 
     * TEL assigned identifier, which may not be correct, but may still be useful.
     */
    TEL_UNCERTAIN,
    /**
     * The identifier in the source library's authority file
     */
    LIBRARY_AUTHORITY_FILE,
    /**
     * Id in VIAF
     */
    VIAF,
    /**
     * International Standard Name Identifier
     */
    ISNI,
    /**
     * URL for the homepage of the party
     */
    URL,
    /**
     * MACS the party
     */
    MACS;
}
