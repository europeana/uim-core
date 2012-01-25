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
package eu.europeana.uim.repox;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Interface declaration of the Repox service for UIM. Basically, Repox only writes additional
 * information back to the UIM objects, but the UIM objects are used to update/delete information in
 * Repox.
 * 
 * @author Georgios Markakis
 * @since Jan 20, 2012
 */
public interface RepoxService {
    /**
     * Deletes a provider from Repox (Collection objects belonging to this provider are deleted as
     * well)
     * 
     * @param provider
     *            UIM provider object to delete Repox providers
     * @throws RepoxException
     */
    void deleteProvider(Provider<?> provider) throws RepoxException;

    /**
     * Updates a provider within Repox
     * 
     * @param provider
     *            UIM provider object to update/create on Repox side
     * @throws RepoxException
     */
    void updateProvider(Provider<?> provider) throws RepoxException;

//    /**
//     * Updates a provider within Repox
//     * 
//     * @param provider
//     *            UIM provider object to update/create on Repox side
//     * @throws RepoxException
//     */
//    void synchronizeProvider(Provider<?> provider) throws RepoxException;
    
    /**
     * Delete a collection within Repox (data source)
     * 
     * @param collection
     *            UIM Collection object to be deleted
     * @throws RepoxException
     */
    void deleteCollection(Collection<?> collection) throws RepoxException;

    /**
     * Update a collection within Repox (data source)
     * 
     * @param collection
     *            UIM Collection object to be updated
     * @throws RepoxException
     */
    void updateCollection(Collection<?> collection) throws RepoxException;

//    /**
//     * Update a collection within Repox (data source)
//     * 
//     * @param collection
//     *            UIM Collection object to be updated
//     * @throws RepoxException
//     */
//    void synchronizeCollection(Collection<?> collection) throws RepoxException;
//    
//    /**
//     * Check the status of an existing harvesting job
//     * 
//     * @param col
//     *            which collection
//     * @return status
//     * @throws RepoxException
//     */
//    RepoxHarvestingStatus getHarvestingStatus(Collection<?> col) throws RepoxException;
//
//    /**
//     * Gets the latest harvesting Log for a specific DataSource
//     * 
//     * @param col
//     *            a UIM Collection object reference
//     * @return the HarvestLog
//     * @throws RepoxException
//     */
//    String getHarvestLog(Collection<?> col) throws RepoxException;
}
