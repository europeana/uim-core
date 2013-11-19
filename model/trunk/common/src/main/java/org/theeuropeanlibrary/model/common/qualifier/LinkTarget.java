/* LinkType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Qualifier for resource identifiers.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public enum LinkTarget {
    /**
     * A link to an image representative of the resource
     */
    THUMBNAIL,
    /**
     * A link to the bibliographic record display in a catalogue (same as tel:seeOnline or
     * europeana:isShownAt)
     */
    CATALOGUE_RECORD,
    /**
     * A link to the lod record display (should lead to RDF)
     */
    LOD_RECORD,
    /**
     * A link to the table of contents
     */
    TABLE_OF_CONTENTS,
    /**
     * a direct link to the digital object (same as europeana:isShownBy)
     */
    DIGITAL_OBJECT,
    /**
     * a link to the digital object shown in context (same as europeana:isShownAt)
     */
    DIGITAL_OBJECT_AT,
    /**
     * A link, such as those specified in the Europeana Rights Guidelines
     */
    RIGHTS,
    /**
     * A link to a related resource. The kind of relation may be specified by a ResourceRelation
     * qualifier
     */
    RELATED_RESOURCE,
    /**
     * A link to a resource to which we want give attribution
     */
    ATTRIBUTION,

    /**
     * A link to a resource holding full text
     */
    FULL_TEXT,
    /**
     * A link to an image
     */
    IMAGE,
    /**
     * A link to an image on an image server
     */
    IMAGE_SERVER
}
