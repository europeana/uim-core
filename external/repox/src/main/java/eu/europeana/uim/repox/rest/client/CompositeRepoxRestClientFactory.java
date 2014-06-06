/* RepoxRestClientFactoryImpl.java - created on Jan 24, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.client;

import java.util.HashMap;
import java.util.Map;

import eu.europeana.uim.repox.client.RepoxRestClient;
import eu.europeana.uim.repox.client.RepoxRestClientFactory;

/**
 * Implementation for {@link RepoxRestClientFactory} providing {@link CompositeRepoxRestClient} for
 * specific urls.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 24, 2012
 */
public class CompositeRepoxRestClientFactory implements RepoxRestClientFactory {
    /**
     * lookup map for repox clients per url
     */
    private Map<String, CompositeRepoxRestClient> lookup;

    /**
     * Creates a new instance of this class.
     */
    public CompositeRepoxRestClientFactory() {
        lookup = new HashMap<String, CompositeRepoxRestClient>();
    }

    @Override
    public RepoxRestClient getInstance(String url) {
        String localUrl = url;
        localUrl = localUrl.replace("OAIHandler", "rest");

        CompositeRepoxRestClient client = lookup.get(localUrl);
        if (client == null) {
            client = new CompositeRepoxRestClient(localUrl);
            lookup.put(localUrl, client);
        }
        return client;
    }
}
