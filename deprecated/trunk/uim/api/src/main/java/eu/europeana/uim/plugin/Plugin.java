/* Plugin.java - created on Sep 19, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.plugin;

import java.util.List;

/**
 * Generic plugin providing identification methods, parameter settings and thread counts besides the
 * methods to initialize and shutting it down.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 19, 2012
 */
public interface Plugin {
    /**
     * A unique identifier used to register the plugin with the registry.
     * 
     * @return identifier for this plugin (should be Plugin.class.getSimpleName()).
     */
    String getIdentifier();

    /**
     * Get a useful name of the plugin to clarify meaning of a plugin.
     * 
     * @return name for this plugin
     */
    String getName();

    /**
     * Get the description of the plugin which is provided to the operators when starting analyzing
     * workflows.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * Initialize the plugin when it is loaded in the OSGI container and attached to the uim
     * registry.
     */
    void initialize();

    /**
     * Shutdown teh plugin when it is removed from the uim registry (due to OSGI shutdown or
     * reinstallation etc.
     */
    void shutdown();

    /**
     * List of configuration parameters this plugin can take from the execution context to be
     * configured for a specific execution. As a policy the parameters must follow a certain schema
     * to qualify as a valid parameter, namely prefixed by the identifier of the plugin, the name of
     * the parameter, and the type of the value as simple value of the class (NOTE: points are not
     * valid inside the name only dashes).
     * 
     * NOTE: any execution related configuration/information needs to be stored into the context and
     * retrieved from the context during the processRecord method.
     * 
     * @return list of configuration parameters.
     */
    List<String> getParameters();

    /**
     * A plugin is always executed within a thread pool, this parameter defines the preferred size
     * of the pool. Plugins should know best, what's a good level of parallelism.
     * 
     * @return number of threads this plugin should usually be processed.
     */
    int getPreferredThreadCount();

    /**
     * Number of maximum threads. The plugin might specify here one (1) if it is not thread safe.
     * 
     * @return the number of maximal threads.
     */
    int getMaximumThreadCount();
}
