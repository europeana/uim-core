/* HyperElement.java - created on Apr 11, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.graph;

/**
 * This element serves as base class for the elements (nodes, edges) in the {@link HyperGraph}. It
 * basically provides only an identifier, but might be extended in the future to provide a payload.
 * 
 * @param <ID>
 *            generic identifier type
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 11, 2012
 */
public interface HyperElement<ID> {
    /**
     * @return unique identifier of the element
     */
    ID getId();
}
