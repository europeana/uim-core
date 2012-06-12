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

import eu.europeana.uim.api.AbstractIngestionPlugin;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.common.TKey;

/**
 * Base class for linking and thumbnail checking.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 20, 2011
 */
public abstract class AbstractLinkIngestionPlugin extends AbstractIngestionPlugin {
    /**
     * Set the Logging variable to use logging within this class
     */
    private static final Logger                                    log  = Logger.getLogger(AbstractLinkIngestionPlugin.class.getName());

    /**
     * typed key to retrieve the container holding all execution dependent variables
     */
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
        HttpClientSetup.getHttpClient().getConnectionManager().closeIdleConnections(2, TimeUnit.SECONDS);
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
    public <I> void completed(ExecutionContext<I> context) throws IngestionPluginFailedException {
        Data value = context.getValue(DATA);
        log.info("Submitted:" + value.submitted + ", Ignored: " + value.ignored);
    }

    /**
     * Container holding all execution specific information for the validation plugin.
     */
    protected static class Data implements Serializable {
        public int             ignored    = 0;
        public int             submitted  = 0;

        // Set<TKey<?, String>> checkurls = new HashSet<TKey<?, String>>();
        public Set<LinkTarget> checktypes = new HashSet<LinkTarget>();
        public File            directory;
    }
}
