/* ClusterEdgeType.java - created on Oct 23, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.cluster;

/**
 * Supported edge types representing clusters like manifest.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Oct 23, 2012
 */
public enum ClusterType {
    /**
     * similar clusters
     */
    SIMILAR,
    /**
     * duplicate clusters
     */
    DUPLICATE,
    /**
     * work clusters
     */
    WORK,
    /**
     * manifest clusters
     */
    MANIFEST,
    /**
     * arrow primary work clusters
     */
    ARROW_PRIMARY_WORK,
    /**
     * arrow secondary work clusters
     */
    ARROW_SECONDARY_WORK,
    /**
     * arrow manifest clusters
     */
    ARROW_MANIFEST;
}
