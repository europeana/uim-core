/* ExecutionUimPlugin.java - created on Sep 19, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.plugin;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.UimDataSet;

/**
 * This plugin incorporates in addition function for initialization and shutting
 * down on a {@link ExecutionContext} level.
 *
 * @param <U> uim data set type
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 19, 2012
 */
public interface ExecutionPlugin<U extends UimDataSet<I>, I> extends Plugin {

    /**
     * Initialization method for an execution context. The context holds the
     * properties specific for this execution.
     *
     * @param context holds execution depending, information the
     * {@link ExecutionContext} for this processing call. This context can
     * change for each call, so references to it have to be handled carefully.
     * @throws IngestionPluginFailedException is thrown if the intiliazation
     * fails and so the plugin is not ready to take records for this
     * {@link ExecutionContext}
     */
    void initialize(ExecutionContext<U, I> context) throws IngestionPluginFailedException;

    /**
     * Finalization method (tear down) for an execution. At the end of each
     * execution this method is called to allow the plugin to clean up memory or
     * external resources.
     *
     * @param context holds execution depending, information the
     * {@link ExecutionContext} for this processing call. This context can
     * change for each call, so references to it have to be handled carefully.
     * @throws IngestionPluginFailedException is thrown if the tear down
     * encountered a severe failure during deleting external resources, so that
     * executing it in a new {@link ExecutionContext} will most likely fail
     */
    void completed(ExecutionContext<U, I> context) throws IngestionPluginFailedException;
}
