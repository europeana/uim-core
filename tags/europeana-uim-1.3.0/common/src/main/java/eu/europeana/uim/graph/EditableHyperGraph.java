/* EditableHyperGraph.java - created on Apr 11, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.graph;

/**
 * This interface extends the read-only {@link HyperGraph} to provide manipulation of the content
 * (creating, updating and deleting edges and nodes).
 * 
 * @param <ID>
 *            generic identifier type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 11, 2012
 */
public interface EditableHyperGraph<ID> extends HyperGraph<ID> {
    /**
     * Creates a new {@link HyperNode} of the specific type. The node is stored in the graph but is
     * not connected to any other node. If the id already exists, the existing node will be
     * returned.
     * 
     * @param id
     *            unique identifier for the node
     * @return new node or existing one for the id
     */
    HyperNode<ID> createNode(ID id);

    /**
     * Creates a new directed {@link HyperEdge} of the specified type from source to target node.
     * 
     * @param id
     *            unique identifier for the edge
     * @param type
     *            allowed type of the edges
     * @param source
     *            source node id
     * @param target
     *            target node id
     * @return new edge or existing one
     */
    HyperEdge<ID> connect(Enum<?> type, ID id, ID source, ID target);

    /**
     * Removes the given node or edge. Note, while removing an edge just removes the edge, removing
     * a node also removes all edges connected to this node implicitly.
     * 
     * @param elementId
     *            id of an element in the graph (node or edge) to be removed
     */
    void remove(ID elementId);

    /**
     * Updates the given element (node or edge) in the graph.
     * 
     * @param element
     *            element to be updated
     */
    void update(HyperElement<ID> element);

    /**
     * Truncates the full graph.
     */
    void truncate();
}
