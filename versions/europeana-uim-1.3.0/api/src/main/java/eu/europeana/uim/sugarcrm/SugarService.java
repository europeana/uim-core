/**
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
package eu.europeana.uim.sugarcrm;

import java.util.List;
import java.util.Map;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * This is the main interface for the OSGI based SugarCrm plugin OSGI service.
 * 
 * @author Georgios Markakis
 */
public interface SugarService {
    /**
     * Logins and ensures a new session
     * 
     * @return the sessionID
     * @throws SugarException
     */
    String login() throws SugarException;

    /**
     * @throws SugarException
     */
    void logout() throws SugarException;

    /**
     * @return true iff there is a valid session
     * @throws SugarException
     */
    boolean hasActiveSession() throws SugarException;

    /**
     * Update a provider in Sugar
     * 
     * @param provider
     *            UIM provider object to update/create on Sugar side
     * @throws SugarException
     */
    void updateProvider(Provider<?> provider) throws SugarException;

    /**
     * Reads information from sugar to a UIM provider (data source)
     * 
     * @param provider
     *            UIM provider object to update/create on UIM side
     * @return true iff a value in the provider bean has been changed.
     * @throws SugarException
     */
    boolean synchronizeProvider(Provider<?> provider) throws SugarException;

    /**
     * Reads information from map to a UIM provider (data source)
     * 
     * @param provider
     *            UIM provider object to update/create on UIM side
     * @param values
     *            the values to populate UIM from
     * @return true iff a value in the provider bean has been changed.
     * @throws SugarException
     */
    boolean synchronizeProvider(Provider<?> provider, Map<String, String> values)
            throws SugarException;

    /**
     * @param activeOnly
     * @return a list of all providers in sugar
     * @throws SugarException
     */
    List<Map<String, String>> listProviders(boolean activeOnly) throws SugarException;

    /**
     * @param mnemonic
     *            of the collection
     * @return the provider mnemonic the collection belongs to null if not found
     * @throws SugarException
     */
    String getProviderForCollection(String mnemonic) throws SugarException;

    /**
     * Update a collection in Sugar
     * 
     * @param collection
     *            UIM Collection object to be updated
     * @throws SugarException
     */
    void updateCollection(Collection<?> collection) throws SugarException;

    /**
     * Reads information from sugar to a UIM collection (data source)
     * 
     * @param collection
     *            UIM Collection object to be updated
     * @return true iff a value in the collection bean has been changed.
     * @throws SugarException
     */
    boolean synchronizeCollection(Collection<?> collection) throws SugarException;

    /**
     * Reads information from sugar to a UIM collection (data source)
     * 
     * @param collection
     *            UIM Collection object to be updated
     * @param values
     * @return true iff a value in the collection bean has been changed.
     * @throws SugarException
     */
    boolean synchronizeCollection(Collection<?> collection, Map<String, String> values)
            throws SugarException;

    /**
     * @param values
     * @return the mnemonic value of the collection
     * @throws SugarException
     */
    String getCollectionMnemonic(Map<String, String> values) throws SugarException;

    /**
     * @param values
     * @return the mnemonic value of the provider
     * @throws SugarException
     */
    String getProviderMnemonic(Map<String, String> values) throws SugarException;

    /**
     * @param activeOnly
     * @return a list of all collections in sugar
     * @throws SugarException
     */
    List<Map<String, String>> listCollections(boolean activeOnly) throws SugarException;
}
