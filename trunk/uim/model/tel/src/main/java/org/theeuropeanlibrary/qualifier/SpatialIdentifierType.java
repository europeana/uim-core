/* SpatialIdentifierType.java - created on 23 de Mar de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * Identifiers of Geographic entities supported by TEL
 * 
 * MARC country codes (http://www.loc.gov/marc/countries/) are not supported, because they should be
 * converted to ISO-3166
 * 
 * The following are not support may be of interest in the future:
 * 
 * The id in TGN - Thesaurus of Geographic Names
 * http://www.getty.edu/research/tools/vocabularies/tgn/index.html
 * 
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 23 de Mar de 2011
 */
public enum SpatialIdentifierType {
    /**
     * Identifier assigned by TEL
     */
    TEL,
    /**
     * The id in geonames http://www.geonames.org/
     */
    GEONAMES,
    /**
     * Any code of the ISO3166 familly (ISO3166-1-alpha-2, ISO3166-1-alpha-3, ISO3166-2)
     */
    ISO3166,
    /**
     * Getty Thesaurus of Geographic Names
     */
    TGN,
    /**
     * The id in the national library authority file
     */
    LIBRARY_AUTHORITY_FILE
}
