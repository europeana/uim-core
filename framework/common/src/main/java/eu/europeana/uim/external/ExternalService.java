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
package eu.europeana.uim.external;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * This interface defines common functionality to all external services. This can be used to
 * synchronize between the UIM and other services like SugarCRM and Repox.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 2, 2014
 */
public interface ExternalService {
    /**
     * External service should synchronize a provider or in case of the delete flag delete it.
     * 
     * @param provider
     *            UIM provider object to be synchronized
     * @param deleted
     *            Is the provider deleted and should be removed from the external service?
     * @return Return true, if the synchronization actually changed anything
     * @throws ExternalServiceException
     */
    boolean synchronize(Provider<?> provider, boolean deleted) throws ExternalServiceException;

    /**
     * External service should synchronize a collection or in case of the delete flag delete it.
     * 
     * @param collection
     *            UIM Collection object to be synchronized
     * @param deleted
     *            Is the provider deleted and should be removed from the external service?
     *            @return Return true, if the synchronization actually changed anything
     * @throws ExternalServiceException
     */
    boolean synchronize(Collection<?> collection, boolean deleted) throws ExternalServiceException;
}
