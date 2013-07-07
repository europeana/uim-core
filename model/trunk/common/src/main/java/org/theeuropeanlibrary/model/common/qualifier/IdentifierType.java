/* IdentifierType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Qualifier for resource identifiers.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum IdentifierType {
    /**
     * ISBN
     */
    ISBN,
    /**
     * ISSN
     */
    ISSN,
    /**
     * an URN (if the URN represents an ISBN or ISSN, than the ISBN/ISBN should be extracted from
     * the URN
     */
    URN,
    /**
     * DOI - Digital Object Identifier
     */
    DOI,
    /**
     * Legal deposit number
     */
    LEGAL_DEPOSIT,
    /**
     * a general URI (should be used only if the specific type of URI is not known (for example
     * URL))
     */
    URI,
    /**
     * URL
     */
    URL,
    /**
     * Serial Item and Contribution Identifier
     */
    SICI,
    /**
     * An URL linking to an OpenURL service
     */
    OPEN_URL,
    /**
     * A persistent URL (see purl.org)
     */
    PURL,
    /**
     * NBN - National bibliography number
     */
    NBN,
    /**
     * A identifier unique in the local system at the data provider (e.g. Control number)
     */
    LOCAL_IDENTIFIER,
    /**
     * The identifier is unknown or was not specified
     */
    OTHER,
    /**
     * The identifier used in the oaipmh frame
     */
    OAIPMH,
    /**
     * This identifier should be used to display the EOD button, and be sent to the EOD service link
     * This identifier should not be displayed. If it is to be displayed, the identifier is
     * duplicated in another identifier object in the record.
     */
    EOD,
    /** info:ark */
    ARK,
    /** info:arxiv/ */
    ARXIV,
    /** info:bibcode/ */
    BIBCODE,
    /** info:bnf/ */
    BNF,
    /** info:dlf/ Digital Library Federation Identifiers */
    DLF,
    /** info:eu-repo/  identifiers used by the European Repository Systems */
    EU_REPO,
    /**  Fedora Digital Objects and Disseminations */
    FEDORA,
    /** Handles */
    HANDLE,
    /** Repository of the LANL Research Library */
    IANL,
    /** Library of Congress Identifiers */
    LC,
    /** Library of Congress Control Numbers */
    LCCN,
    /** OCLC Worldcat Control Numbers */
    OCLC,
    /** PUBMED */
    PMID,
    /** PRONOM Unique Identifiers */
    PRONOM,
    /**  Registry Framework Architecture Identifiers (RFA) */
    RFA,
    /**  Patent Control Information */
    PATENT_CONTROL,
    /**  */
    NATIONAL_BIBLIOGRAPHIC_AGENCY,
    /**  */
    COPYRIGHT_ARTICLE_FEE_CODE,
    /** INTERNATIONAL_STANDARD_RECORDING_CODE */
    ISRC,
    /** UNIVERSAL_PRODUCT_CODE */
    UNIVERSAL_PRODUCT_CODE,
    /** ISMN International Standard Music Number*/
    ISMN,
    /** INTERNATIONAL_ARTICLE_NUMBER  */
    INTERNATIONAL_ARTICLE_NUMBER,
    /** Fingerprint Identifier  */
    FINGERPRINT_IDENTIFIER,
    /** Standard Technical Report Number */
    STRN,
    /** PUBLISHER_NUMBER */
    PUBLISHER_NUMBER,
    /** CODEN */
    CODEN,
    /** A control number in another system */
    SYSTEM_CONTROL_NUMBER,
    /** Original Study Number for Computer Data Files*/
    ORIGINAL_STUDY_NUMBER_FOR_DATA_FILES,
    /** Source of Acquisition */
    SOURCE_OF_ACQUISITION,
    /** GOVERNMENT_PUBLICATION_NUMBER */
    GOVERNMENT_PUBLICATION_NUMBER,
    /** DISSERTATION IDENTIFIER (from marc21)*/
    DISSERTATION,
    /**
     * identifier representing cluster group (detailed types are insided identifier)
     */
    CLUSTER
}
