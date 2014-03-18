package eu.europeana.uim.model;

import eu.europeana.uim.common.TKey;

/**
 * Registry holding all known keys for the skos like concept model of metadata on a record.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
@SuppressWarnings("all")
public final class GlobalModelRegistry {

    /** The key for all sorts of concepts.
     */
    public static final TKey<GlobalModelRegistry, Concept>  CONCEPT  = TKey.register(
                                                                             GlobalModelRegistry.class,
                                                                             "concept",
                                                                             Concept.class);

    /** The key representing agents
     */
    public static final TKey<GlobalModelRegistry, Agent>    AGENT    = TKey.register(
                                                                             GlobalModelRegistry.class,
                                                                             "agent", Agent.class);

    
    /** The key representing temporal aspects 
     */
    public static final TKey<GlobalModelRegistry, Temporal> TEMPORAL = TKey.register(
                                                                             GlobalModelRegistry.class,
                                                                             "temporal",
                                                                             Temporal.class);

    
    /** The key representing spatial aspects
     */
    public static final TKey<GlobalModelRegistry, Spatial>  SPATIAL  = TKey.register(
                                                                             GlobalModelRegistry.class,
                                                                             "spatial",
                                                                             Spatial.class);

}
