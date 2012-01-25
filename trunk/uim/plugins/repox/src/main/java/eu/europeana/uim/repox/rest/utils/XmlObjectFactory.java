/* ObjectFiller.java - created on Jan 24, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.utils;

import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Source;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Interface to fill additonal information from UIM objects into repox objects.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 24, 2012
 */
public interface XmlObjectFactory {
    /**
     * Fill aggregator with additional information from provider.
     * 
     * @param provider
     * @return aggregator
     */
    Aggregator createAggregator(Provider<?> provider);

    /**
     * Fill jaxb provider with additional information from provider.
     * 
     * @param provider
     * @return jaxbProvider
     */
    eu.europeana.uim.repox.rest.client.xml.Provider createProvider(Provider<?> provider);

    /**
     * Fill source with additional information from collection.
     * 
     * @param collection
     * @return datasource
     */
    Source createDataSource(Collection<?> collection);
}
