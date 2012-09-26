/* RepoxRestClientFactory.java - created on Jan 24, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.client;

/**
 * Factory to retrieve instances for specific base urls specifying Repox installations.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 24, 2012
 */
public interface RepoxRestClientFactory {
    /**
     * @param url
     *            determining location of a Repox installation
     * @return instance of repox client connected to given url
     */
    RepoxRestClient getInstance(String url);
}
