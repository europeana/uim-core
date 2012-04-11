/* HyperGraph.java - created on Apr 11, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.graph;

/**
 * This interface defines a hyper graph. A hypergraph can have multiple edges between nodes. Unique
 * identifiers for both nodes and edges are generic. The graph is directed.
 * 
 * @param <ID>
 *            generic identifier type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 11, 2012
 */
public interface HyperGraph<ID> {
    /**
     * Provides the direction of the edges (incoming, outgoing, both).
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since Apr 11, 2012
     */
    public enum EdgeDirection {
        /** direction for incoming edges for a specific node */
        INCOMING,
        /** direction for outgoing edges for a specific node */
        OUTGOING,
        /** direction for outgoing/incoming edges for a specific node */
        BOTH
    }

    /**
     * Returns nodes for the given ids or null at the position in the arrary.
     * 
     * @param ids
     *            ids of the nodes
     * @return nodes
     */
    HyperNode<ID>[] getNodes(ID... ids);

    /**
     * Returns edges for the given ids or null at the position in the arrary.
     * 
     * @param ids
     *            ids of the edges
     * @return edges
     */
    HyperNode<ID>[] getEdges(ID... ids);

    /**
     * Returns the edge from source and target with the given type or null if it not exists.
     * 
     * @param type
     *            allowed type of the edges
     * @param source
     *            source node id
     * @param target
     *            target node id
     * @return edge or null
     */
    HyperEdge<ID> getEdge(Enum<?> type, ID source, ID target);

    /**
     * Returns all edge ids in the direction specified relative to the source node id. If no types
     * are given all edges fitting the direction are returned.
     * 
     * @param source
     *            source node id
     * @param direction
     *            direction of the edges
     * @param types
     *            allowed types of the edges
     * @return edge ids matching the criteria
     */
    Iterable<ID> getEdgeIds(ID source, EdgeDirection direction, Enum<?>... types);

    /**
     * Returns all neighbouring node ids of the given source node id connected by edges fullfilling
     * the given criteria.
     * 
     * @param source
     *            source node id
     * @param direction
     *            direction of the edges
     * @param types
     *            allowed types of the edges
     * @return node IDs matching the criteria
     */
    Iterable<ID> getNodeIds(ID source, EdgeDirection direction, Enum<?>... types);

    /**
     * @return all node ids in the graph
     */
    Iterable<ID> getAllNodeIds();

    /**
     * @param types
     *            allowed types of the edges
     * @return all edge ids in the graph matching the optional types
     */
    Iterable<ID> getAllEdgeIds(Enum<?>... types);
}
