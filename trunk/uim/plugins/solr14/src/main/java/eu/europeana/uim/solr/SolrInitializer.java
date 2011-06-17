/* RepositoryInitializer.java - created on Feb 23, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.solr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.core.CoreContainer;

import eu.europeana.uim.common.BlockingInitializer;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 23, 2011
 */
public class SolrInitializer extends BlockingInitializer {

    private static final Logger log = Logger.getLogger(SolrInitializer.class.getName());

    private SolrServer          server;
    private CoreContainer       container;

    private final String        url;
    private final String        core;

    /**
     * Creates a new instance of this class.
     * 
     * @param url
     * @param core
     */
    public SolrInitializer(String url, String core) {
        this.url = url;
        this.core = core;
    }

    /**
     * @return the solr server
     */
    public SolrServer getServer() {
        return server;
    }

    /**
     * @return the curren tcontainter
     */
    public CoreContainer getContainer() {
        return container;
    }

    /**
     * @param container
     */
    public void setContainer(CoreContainer container) {
        this.container = container;
    }

    @Override
    protected void initializeInternal() {
        try {
            status = STATUS_BOOTING;
            if (url.startsWith("file://")) {
                File home = new File(url.substring(7));
                container = new CoreContainer();
                container.load(home.getAbsolutePath(), new File(home, "solr.xml"));
                server = new EmbeddedSolrServer(container, core);
            } else {
                server = new CommonsHttpSolrServer(new URL(url) + core);
            }

            status = STATUS_INITIALIZED;
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to initialize repository at <" + url + ">", t);
            status = STATUS_FAILED;

            throw new RuntimeException("Failed to setup core <" + core + "> at <" + url + ">");
        }
    }

}
