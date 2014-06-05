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

/**
 * Interface declaration of the Repox REST client OSGI service
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface RepoxRestClient extends AggregatorRepoxRestClient, ProviderRepoxRestClient,
        DataSourceRepoxRestClient, RecordRepoxRestClient, HarvestingRepoxRestClient,
        DataSourceFunctionsRepoxRestClient {
    /**
     * Gets the base URI for the client.
     * 
     * @return the http address where the REPOX connected to this instance resides
     */
    String getUri();
}
