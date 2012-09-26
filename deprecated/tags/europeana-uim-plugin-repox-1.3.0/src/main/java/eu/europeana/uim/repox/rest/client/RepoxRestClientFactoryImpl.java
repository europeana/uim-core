/* RepoxRestClientFactoryImpl.java - created on Jan 24, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for {@link RepoxRestClientFactory} providing {@link RepoxRestClientImpl} for
 * specific urls.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 24, 2012
 */
public class RepoxRestClientFactoryImpl implements RepoxRestClientFactory {
    /**
     * lookup map for repox clients per url
     */
    private Map<String, RepoxRestClientImpl> lookup;

    /**
     * Creates a new instance of this class.
     */
    public RepoxRestClientFactoryImpl() {
        lookup = new HashMap<String, RepoxRestClientImpl>();
    }

    @Override
    public RepoxRestClient getInstance(String url) {
        String localUrl = url;
        localUrl = localUrl.replace("OAIHandler", "rest");

        RepoxRestClientImpl client = lookup.get(localUrl);
        if (client == null) {
            client = new RepoxRestClientImpl();
            client.setUri(localUrl);
            lookup.put(localUrl, client);
        }
        return client;
    }
}
