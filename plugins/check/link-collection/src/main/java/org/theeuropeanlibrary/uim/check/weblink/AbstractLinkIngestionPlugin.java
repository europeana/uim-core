/* LinkcheckIngestionPlugin.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.uim.check.weblink.http.HttpClientSetup;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.Collection;

/**
 * Base class for linking and thumbnail checking.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 20, 2011
 */
public abstract class AbstractLinkIngestionPlugin<I> extends
        AbstractIngestionPlugin<Collection<I>, I> {
    /**
     * Set the Logging variable to use logging within this class
     */
    private static final Logger                                    log  = Logger.getLogger(AbstractLinkIngestionPlugin.class.getName());

    /**
     * typed key to retrieve the container holding all execution dependent variables
     */
    @SuppressWarnings("rawtypes")
    protected static final TKey<AbstractLinkIngestionPlugin, Data> DATA = TKey.register(
                                                                                AbstractLinkIngestionPlugin.class,
                                                                                "data", Data.class);

    /**
     * Creates a new instance of this class and initializes members.
     * 
     * @param name
     *            meaningful name of this plugin
     * @param description
     *            meaningful description of this plugin
     */
    public AbstractLinkIngestionPlugin(String name, String description) {
        super(name, description);
    }

    @Override
    public TKey<?, ?>[] getInputFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOptionalFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOutputFields() {
        return new TKey[0];
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
        HttpClientSetup.getHttpClient().getConnectionManager().closeIdleConnections(2,
                TimeUnit.SECONDS);
    }

    @Override
    public int getPreferredThreadCount() {
        return 10;
    }

    @Override
    public int getMaximumThreadCount() {
        return 10;
    }

    @Override
    public void completed(ExecutionContext<Collection<I>, I> context)
            throws IngestionPluginFailedException {
        Data value = context.getValue(DATA);
        log.info("Submitted:" + value.submitted + ", Ignored: " + value.ignored);
    }

    /**
     * Container holding all execution specific information for the validation plugin.
     */
    public static class Data implements Serializable {
        int             ignored    = 0;
        int             submitted  = 0;

        // Set<TKey<?, String>> checkurls = new HashSet<TKey<?, String>>();
        Set<LinkTarget> checktypes = new HashSet<LinkTarget>();
        File            directory;
    }
}
