/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.repox.rest.client;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.DataProviders;
import eu.europeana.uim.repox.rest.client.xml.Provider;

/**
 * Interface declaration of the Repox REST client OSGI service for provider specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface ProviderRepoxRestClient {
    /**
     * Creates a provider in Repox and assigns it to the specific Aggregator
     * 
     * @param prov
     *            Provider definition
     * @param agr
     *            Aggregator reference
     * @return created provider
     * @throws RepoxException
     */
    Provider createProvider(Provider prov, Aggregator agr) throws RepoxException;

    /**
     * Moves a provider in Repox and assigns it to the new Aggregator
     * 
     * @param providerId
     *            provider id
     * @param aggregatorId
     *            aggregator id
     * @return created provider
     * @throws RepoxException
     */
    Provider moveProvider(String providerId, String aggregatorId) throws RepoxException;

    /**
     * Deletes a provider from Repox
     * 
     * @param providerId
     *            the Provider reference
     * @return successful?
     * @throws RepoxException
     */
    String deleteProvider(String providerId) throws RepoxException;

    /**
     * Updates a provider within Repox
     * 
     * @param prov
     *            Provider object to update
     * @return updated provider
     * @throws RepoxException
     */
    Provider updateProvider(Provider prov) throws RepoxException;

    /**
     * Retrieves all available providers within Repox
     * 
     * @return an object containing all provider references
     * @throws RepoxException
     */
    DataProviders retrieveProviders() throws RepoxException;

    /**
     * Retrieves all available providers within Repox given a specific Aggregator
     * 
     * @param agr
     *            Aggregator reference
     * @return an object containing all provider references
     * @throws RepoxException
     */
    DataProviders retrieveAggregatorProviders(Aggregator agr) throws RepoxException;

    /**
     * Retrieve a Provider given a specific Id
     * 
     * @param providerId
     * @return specific provider
     * @throws RepoxException
     */
    eu.europeana.uim.repox.rest.client.xml.Provider retrieveProvider(String providerId)
            throws RepoxException;

    /**
     * Retrieve a Provider given a specific mnemonic
     * 
     * @param mnemonic
     * @return specific provider
     * @throws RepoxException
     */
    eu.europeana.uim.repox.rest.client.xml.Provider retrieveProviderByMetadata(String mnemonic)
            throws RepoxException;
}
