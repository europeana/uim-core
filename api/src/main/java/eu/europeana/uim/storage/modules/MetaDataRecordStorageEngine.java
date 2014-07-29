package eu.europeana.uim.storage.modules;

import java.util.List;

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
public interface MetaDataRecordStorageEngine<I> {

    /**
     * @param collection
     * @return creates a new record for the given external identifier (might
     * differ from internal one)
     * @throws StorageEngineException
     */
    MetaDataRecord<I> createMetaDataRecord(Collection<I> collection)
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
     * @param record
     * @throws StorageEngineException
     */
    void updateMetaDataRecord(MetaDataRecord<I> record) throws StorageEngineException;

    /**
     * @param id unique ID
     * @return metadata record for the provided ID
     * @throws StorageEngineException
     */
    MetaDataRecord<I> getMetaDataRecord(I id) throws StorageEngineException;
    
    /**
     * @param ids unique IDs, unique over collection, provider, ...
     * @return metadata records for the provided IDs
     * @throws StorageEngineException
     */
    List<MetaDataRecord<I>> getMetaDataRecords(List<I> ids) throws StorageEngineException;
}
