package eu.europeana.uim.storage.modules;

import java.util.concurrent.BlockingQueue;

import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Request;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface RequestStorageEngine<I> {

    /**
     * @param collection holding this request
     * @return newly created request for the given collection and date
     * @throws StorageEngineException
     */
    Request<I> createRequest(Collection<I> collection) throws StorageEngineException;

    /**
     * Stores the given request and its updated values.
     *
     * @param request
     * @throws StorageEngineException
     */
    void updateRequest(Request<I> request) throws StorageEngineException;

    /**
     * @param id unique ID, unique over collection, provider, ...
     * @return request under the given ID
     * @throws StorageEngineException
     */
    Request<I> getRequest(I id) throws StorageEngineException;

    /**
     * @param mdr the metadata record for which to lookup the request
     * @return all requests where this record was delivered in
     * @throws StorageEngineException
     */
    BlockingQueue<Request<I>> getRequests(MetaDataRecord<I> mdr) throws StorageEngineException;

    /**
     * @param collection
     * @return all requests for the provided collection
     * @throws StorageEngineException
     */
    BlockingQueue<Request<I>> getRequests(Collection<I> collection) throws StorageEngineException;

    /**
     * @param request
     * @return IDs for records for this request
     * @throws StorageEngineException
     */
    BlockingQueue<I> getMetaDataRecordIdsByRequest(Request<I> request) throws StorageEngineException;

    /**
     * @param request
     * @return IDs for records for this request
     * @throws StorageEngineException
     */
    BlockingQueue<MetaDataRecord<I>> getMetaDataRecordsByRequest(Request<I> request)
            throws StorageEngineException;
}
