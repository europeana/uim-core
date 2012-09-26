/* HyperEdge.java - created on Apr 11, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.graph;

/**
 * This interface defines aa edge in the {@link HyperGraph} which is basically an extension of the
 * {@link HyperElement} with an additional type for specifying the kind of connection between nodes
 * represented by an arbitrary enum.
 * 
 * @param <ID>
 *            generic identifier type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 11, 2012
 */
public interface HyperEdge<ID> extends HyperElement<ID> {
    /**
     * @return typed of this represented as generic enum
     */
    Enum<?> getType();

    /**
     * @return source node of this edge
     */
    HyperNode<ID> getSource();

    /**
     * @return target node of this edge
     */
    HyperNode<ID> getTarget();
}
