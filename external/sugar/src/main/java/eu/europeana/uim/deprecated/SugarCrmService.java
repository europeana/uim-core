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
package eu.europeana.uim.deprecated;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.deprecated.model.DatasetStates;
import eu.europeana.uim.deprecated.model.UpdatableField;
import eu.europeana.uim.workflow.Workflow;

/**
 * This is the main interface for the OSGI based SugarCrm plugin OSGI service.
 * 
 * @author Georgios Markakis
 */
public interface SugarCrmService {
    /**
     * This method shows the current connection status of the plugin instance.
     * 
     * @return a ConnectionStatus object
     */
    ConnectionStatus showConnectionStatus();

    /**
     * Assigns the plugin instance a new session under a different user
     * 
     * @param username
     * @param password
     * @return the sessionID
     * @throws LoginFailureException
     */
    String updateSession(String username, String password) throws LoginFailureException;

    /**
     * Changes the Status of a specific record
     * 
     * @param recordId
     *            the Record ID
     * @param state
     *            the state to assign
     * @throws QueryResultException
     */
    void changeEntryStatus(String recordId, DatasetStates state) throws QueryResultException;

    /**
     * Updates a specific record with the specific record values
     * 
     * @param recordID
     *            the Record ID
     * @param values
     *            a map containing <EuropeanaUpdatableField,String> key/value pairs
     * @throws QueryResultException
     */
    void updateRecordData(String recordID, HashMap<UpdatableField, String> values)
            throws QueryResultException;

    /**
     * Updates a specific record with the modifiable fields contained in a SugarCrmRecordImpl object
     * 
     * @param record
     *            the SugarCrmRecordImpl object
     * @throws QueryResultException
     */
    void updateRecordData(SugarCrmRecord record) throws QueryResultException;

    /**
     * Retrieves the records According to a SugarCrmQuery
     * 
     * @param query
     *            the query to perform
     * @return a List<SugarCrmRecordImpl> results
     * @throws QueryResultException
     */
    List<SugarCrmRecord> retrieveRecords(SugarCrmQuery query) throws QueryResultException;

    /**
     * Retrieves a record object by it's ID
     * 
     * @param id
     *            the Record ID
     * @return the corresponding SugarCrmRecordImpl object
     * @throws QueryResultException
     */
    SugarCrmRecord retrieveRecord(String id) throws QueryResultException;

    /**
     * Initializes a workflow in UIM from the data contained in a specific record: this method also
     * creates all the relevant entities into the UIM and sets the record to a specific state after
     * initializing the workflow.
     * 
     * @param worklfowName
     *            the workflow name
     * @param record
     *            the source Record
     * @param endstate
     *            the state to set the record after initializations of the workflow
     * @return a reference to a Workflow object
     * @throws QueryResultException
     * @throws StorageEngineException
     */
    Workflow<?, ?> initWorkflowFromRecord(String worklfowName, SugarCrmRecord record,
            DatasetStates endstate) throws QueryResultException, StorageEngineException;

    /**
     * Initializes a workflow in UIM from the data contained in a specific record: this method also
     * creates all the relevant entities into the UIM and sets the records to a specific state after
     * initializing the workflows.
     * 
     * @param worklfowName
     *            the workflow name
     * @param currentstate
     *            the state of the source records
     * @param endstate
     *            the state to set the record after initialization of the workflows
     * @return a list of references to the created workflow objects
     * @throws QueryResultException
     * @throws StorageEngineException
     */
    List<Workflow<?, ?>> initWorkflowsFromRecords(String worklfowName, DatasetStates currentstate,
            DatasetStates endstate) throws QueryResultException, StorageEngineException;

    /**
     * Update or creates a Provider entity within UIM given a specific record
     * 
     * @param record
     *            the source Record
     * @return the reference to the created Provider
     * @throws StorageEngineException
     */
    Provider<?> updateProviderFromRecord(SugarCrmRecord record) throws StorageEngineException;

    /**
     * Update or creates a Collection entity within UIM given a specific record
     * 
     * @param record
     *            the source Record
     * @param provider
     *            collection's provider
     * @return the reference to the created Provider
     * @throws StorageEngineException
     */
    Collection<?> updateCollectionFromRecord(SugarCrmRecord record, Provider<?> provider)
            throws StorageEngineException;

    /**
     * Adds a note attachment to a specific record
     * 
     * @param recordId
     *            the record ID
     * @param message
     *            the message contents
     * @throws FileAttachmentException
     */
    void addNoteAttachmentToRecord(String recordId, String message) throws FileAttachmentException;

    /**
     * Assigns a listener operation on the running plugin instance
     * 
     * @param id
     * @param listener
     *            an instance of any Class implementing the PollingListener interface
     * @see PollingListener
     */
    void addPollingListener(String id, PollingListener listener);

    /**
     * Removes the specific listener from the queue
     * 
     * @param id
     * @see PollingListener
     */
    void removePollingListener(String id);

    /**
     * Get a list of all the available PollingListeners
     * 
     * @return the list
     * @see PollingListener
     */
    LinkedHashMap<String, PollingListener> getPollingListeners();

    /**
     * Set a list of PollingListeners
     * 
     * @param listeners
     * @see PollingListener
     */
    void setPollingListeners(LinkedHashMap<String, PollingListener> listeners);
}
