/* PartyIdentifierType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * The type of identifiers for parties supported by TEL
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum PartyIdentifierType {
    /** TEL assigned */
    TEL,
    /** The identifier in the source library's authority file */
    LIBRARY_AUTHORITY_FILE,
    /** Id in VIAF */
    VIAF,
    /** Id in MACS if the party was used as a subject */
    MACS,
    /** International Standard Name Identifier */
    ISNI,
    /** URL for the homepage of the party */
    URL;
}
