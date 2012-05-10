package eu.europeana.uim.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;

/**
 * Base class for storage engine typed with a ID class.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface StorageEngine<I> {
    /**
     * @return identifier of the storage engine
     */
    String getIdentifier();

    /**
     * @param config
     *            arbitrary key - value map
     */
    void setConfiguration(Map<String, String> config);

    /**
     * @return configuration as arbitrary key - value map
     */
    Map<String, String> getConfiguration();

    /**
     * Initializes engine by for example opening database connection.
     */
    void initialize();

    /**
     * Shutdown the engine and its connected components like connection to database.
     */
    void shutdown();

    /**
     * Mark a checkpoint.
     */
    void checkpoint();

    /**
     * @param command
     *            arbitrary command interpreted by the engine implementation
     */
    void command(String command);

    /**
     * Finalization method (tear down) for an execution. At the end of each execution this method is
     * called to allow the storage engine to clean up memory or external resources.
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     */
    void completed(ExecutionContext<I> context);

    /**
     * @return status of the engine (starting, ...)
     */
    EngineStatus getStatus();

    /**
     * @return number of hold entries
     */
    // long size();

    /**
     * @return newly created provider
     * @throws StorageEngineException
     */
    Provider<I> createProvider() throws StorageEngineException;

    /**
     * Stores the given provider and its updated values.
     * 
     * @param provider
     * @throws StorageEngineException
     */
    void updateProvider(Provider<I> provider) throws StorageEngineException;

    /**
     * @param id
     *            unique ID, unique over collection, provider, ...
     * @return provider under the given ID
     * @throws StorageEngineException
     */
    Provider<I> getProvider(I id) throws StorageEngineException;

    /**
     * @param mnemonic
     *            defines uniquely a provider
     * @return find a specific provider with the given mnemonic, might not always been supported by
     *         the backend
     * @throws StorageEngineException
     */
    Provider<I> findProvider(String mnemonic) throws StorageEngineException;

    /**
     * @return all known providers
     * @throws StorageEngineException
     */
    List<Provider<I>> getAllProviders() throws StorageEngineException;

    /**
     * @param provider
     *            parenting provider
     * @return newly created collection for the given provider
     * @throws StorageEngineException
     */
    Collection<I> createCollection(Provider<I> provider) throws StorageEngineException;

    /**
     * Stores the given collection and its updated values.
     * 
     * @param collection
     * @throws StorageEngineException
     */
    void updateCollection(Collection<I> collection) throws StorageEngineException;

    /**
     * @param id
     *            unique ID, unique over collection, provider, ...
     * @return collection under the given ID
     * @throws StorageEngineException
     */
    Collection<I> getCollection(I id) throws StorageEngineException;

    /**
     * @param mnemonic
     *            unique name
     * @return find a specific collection with the given mnemonic, might not always been supported
     *         by the backend
     * @throws StorageEngineException
     */
    Collection<I> findCollection(String mnemonic) throws StorageEngineException;

    /**
     * @param provider
     * @return all collections for the given provider or all known if provider is null
     * @throws StorageEngineException
     */
    List<Collection<I>> getCollections(Provider<I> provider) throws StorageEngineException;

    /**
     * @return all collections
     * @throws StorageEngineException
     */
    List<Collection<I>> getAllCollections() throws StorageEngineException;

    /**
     * @param collection
     *            holding this request
     * @param date
     *            when is this request initiatedß
     * @return newly created request for the given collection and date
     * @throws StorageEngineException
     */
    Request<I> createRequest(Collection<I> collection, Date date) throws StorageEngineException;

    /**
     * Stores the given request and its updated values.
     * 
     * @param request
     * @throws StorageEngineException
     */
    void updateRequest(Request<I> request) throws StorageEngineException;

    /**
     * @param id
     *            unique ID, unique over collection, provider, ...
     * @return request under the given ID
     * @throws StorageEngineException
     */
    Request<I> getRequest(I id) throws StorageEngineException;

    /**
     * @param mdr
     *            the metadata record for which to lookup the request
     * @return all requests where this record was delivered in
     * @throws StorageEngineException
     */
    List<Request<I>> getRequests(MetaDataRecord<I> mdr) throws StorageEngineException;

    /**
     * @param collection
     * @return all requests for the provided collection
     * @throws StorageEngineException
     */
    List<Request<I>> getRequests(Collection<I> collection) throws StorageEngineException;

    /**
     * @param collection
     * @param externalIdentifier
     * @return creates a new record for the given external identifier (might differ from internal
     *         one)
     * @throws StorageEngineException
     */
    MetaDataRecord<I> createMetaDataRecord(Collection<I> collection, String externalIdentifier)
            throws StorageEngineException;

    /**
     * Adds the given record request relation.
     * 
     * @param request
     * @param record
     * @throws StorageEngineException
     */
    void addRequestRecord(Request<I> request, MetaDataRecord<I> record)
            throws StorageEngineException;

    /**
     * Stores the given record and its updated values.
     * 
     * @param collection
     * @param record
     * @throws StorageEngineException
     */
    void updateMetaDataRecord(MetaDataRecord<I> record) throws StorageEngineException;

    /**
     * @param dataSet
     * @param workflow
     * @return newly created execution for the given data set and workflow
     * @throws StorageEngineException
     */
    Execution<I> createExecution(UimDataSet<I> dataSet, String workflow)
            throws StorageEngineException;

    /**
     * Stores the given execution and its updated values.
     * 
     * @param execution
     * @throws StorageEngineException
     */
    void updateExecution(Execution<I> execution) throws StorageEngineException;

    /**
     * @param id
     *            unique ID, unique over collection, provider, ...
     * @return execution under the given ID
     * @throws StorageEngineException
     */
    Execution<I> getExecution(I id) throws StorageEngineException;

    /**
     * @return all known executions
     * @throws StorageEngineException
     */
    List<Execution<I>> getAllExecutions() throws StorageEngineException;

    /**
     * @param id
     *            unique ID, unique over collection, provider, ...
     * @return metadata record for the provided ID
     * @throws StorageEngineException
     */
    MetaDataRecord<I> getMetaDataRecord(I id) throws StorageEngineException;

    /**
     * @param ids
     *            unique IDs, unique over collection, provider, ...
     * @return metadata records for the provided IDs
     * @throws StorageEngineException
     */
    List<MetaDataRecord<I>> getMetaDataRecords(List<I> ids) throws StorageEngineException;

    /**
     * @param request
     * @return IDs for records for this request
     * @throws StorageEngineException
     */
    I[] getByRequest(Request<I> request) throws StorageEngineException;

// /**
// * @param request
// * @return IDs for records for this request
// * @throws StorageEngineException
// */
// BlockingQueue<I[]> getBatchesByRequest(Request<I> request) throws StorageEngineException;

    /**
     * @param collection
     * @return IDs for records for this collection
     * @throws StorageEngineException
     */
    I[] getByCollection(Collection<I> collection) throws StorageEngineException;

// /**
// * @param collection
// * @return IDs for records for this collection
// * @throws StorageEngineException
// */
// BlockingQueue<I[]> getBatchesByCollection(Collection<I> collection)
// throws StorageEngineException;

    /**
     * @param provider
     * @param recursive
     * @return IDs for records for this provider
     * @throws StorageEngineException
     */
    I[] getByProvider(Provider<I> provider, boolean recursive) throws StorageEngineException;

// /**
// * @param provider
// * @param recursive
// * @return IDs for records for this provider
// * @throws StorageEngineException
// */
// BlockingQueue<I[]> getBatchesByProvider(Provider<I> provider, boolean recursive)
// throws StorageEngineException;

    /**
     * @return IDs for all known records
     * @throws StorageEngineException
     */
    I[] getAllIds() throws StorageEngineException;

    /**
     * @param request
     * @return number of records for this request
     * @throws StorageEngineException
     */
    int getTotalByRequest(Request<I> request) throws StorageEngineException;

    /**
     * @param collection
     * @return number of records for this collection
     * @throws StorageEngineException
     */
    int getTotalByCollection(Collection<I> collection) throws StorageEngineException;

    /**
     * @param provider
     * @param recursive
     * @return number of records for this provider
     * @throws StorageEngineException
     */
    int getTotalByProvider(Provider<I> provider, boolean recursive) throws StorageEngineException;

    /**
     * @return number of records in this storage
     * @throws StorageEngineException
     */
    int getTotalForAllIds() throws StorageEngineException;

    // potentially simplified storage engine
// String getIdentifier();
// public void setConfiguration(Map<String, String> config);
// public Map<String, String> getConfiguration();
// void initialize();
// void shutdown();
// void checkpoint();
// void command(String command);
// void completed(ExecutionContext context);
// EngineStatus getStatus();
//
// Provider<I> createProvider(String mnemonic) throws StorageEngineException;
// void updateProvider(Provider<I> provider) throws StorageEngineException;
// Provider<I> getProvider(I id) throws StorageEngineException;
// Provider<I> findProvider(String mnemonic) throws StorageEngineException;
// List<Provider<I>> getProviders() throws StorageEngineException;
//
// Collection<I> createCollection(Provider<I> provider, String mnemonic) throws
// StorageEngineException;
// void updateCollection(Collection<I> collection) throws StorageEngineException;
// Collection<I> getCollection(I id) throws StorageEngineException;
// Collection<I> findCollection(String mnemonic) throws StorageEngineException;
// List<Collection<I>> getCollections(Provider<I> provider) throws StorageEngineException;
//
// Request<I> createRequest(Collection<I> collection, Date date) throws StorageEngineException;
// void updateRequest(Request<I> request) throws StorageEngineException;
// Request<I> getRequest(I id) throws StorageEngineException;
// List<Request<I>> getRequests(Collection<I> collection) throws StorageEngineException;
//
// Execution<I> createExecution(DataSet<I> dataSet, String workflow) throws StorageEngineException;
// void updateExecution(Execution<I> execution) throws StorageEngineException;
// Execution<I> getExecution(I id) throws StorageEngineException;
// List<Execution<I>> getExecutions() throws StorageEngineException;
//
// MetaDataRecord<I> createMetaDataRecord(Request<I> request, String externalIdentifier) throws
// StorageEngineException;
// void updateMetaDataRecord(MetaDataRecord<I> record) throws StorageEngineException;
// MetaDataRecord<I> getMetaDataRecord(I id) throws StorageEngineException;
// List<MetaDataRecord<I>> getMetaDataRecords(List<I> ids) throws StorageEngineException;
// List<MetaDataRecord<I>> getMetaDataRecords(Request<I> request, int start, int limit) throws
// StorageEngineException;
// int getMetaDataRecordNumberByRequest(Request<I> request) throws StorageEngineException;
}
