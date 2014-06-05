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
package eu.europeana.uim.repox.client;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Aggregators;

/**
 * Interface declaration of the Repox REST client OSGI service for the aggregator specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface AggregatorRepoxRestClient {
    /**
     * Creates an Aggregator in Repox
     * 
     * @param aggregator
     *            an Aggregator object
     * @return created aggregator
     * @throws RepoxException
     */
    Aggregator createAggregator(Aggregator aggregator) throws RepoxException;

    /**
     * Deletes an existing Aggregator from Repox
     * 
     * @param aggregatorId
     *            an ID to a Aggregator object
     * @return successful?
     * @throws RepoxException
     */
    String deleteAggregator(String aggregatorId) throws RepoxException;

    /**
     * Updates an existing Aggregator in Repox
     * 
     * @param aggregator
     *            the Aggregator object to update
     * @return updated aggregator
     * @throws RepoxException
     */
    Aggregator updateAggregator(Aggregator aggregator) throws RepoxException;

    /**
     * Retrieves all the available Aggregators from Repox
     * 
     * @return an object containing all available Aggregators
     * @throws RepoxException
     */
    Aggregators retrieveAggregators() throws RepoxException;

    /**
     * Retrieves an aggregator for the specific ID from Repox
     * 
     * @param aggregatorId
     *            identifer for an aggregator
     * @return aggregator for ID or null if not existing
     * @throws RepoxException
     */
    Aggregator retrieveAggregator(String aggregatorId) throws RepoxException;

    /**
     * Retrieves an aggregator for the specific ID from Repox
     * 
     * @param mnemonic
     *            identifer for an aggregator
     * @return aggregator for ID or null if not existing
     * @throws RepoxException
     */
    Aggregator retrieveAggregatorByMetadata(String mnemonic) throws RepoxException;
}
