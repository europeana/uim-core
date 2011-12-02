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
     * europeana:isShownAt
     */
    CATALOGUE_RECORD,
    /**
     * A link to the table of contents
     */
    TABLE_OF_CONTENTS,
    /**
     * a direct link to the digital object (same as europeana:isShownBy)
     */
    DIGITAL_OBJECT,
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
    ATTRIBUTION
}
