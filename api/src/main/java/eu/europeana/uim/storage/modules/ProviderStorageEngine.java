package eu.europeana.uim.storage.modules;

import java.util.concurrent.BlockingQueue;

import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface ProviderStorageEngine<I> {

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
     * @param id unique ID, unique over collection, provider, ...
     * @return provider under the given ID
     * @throws StorageEngineException
     */
    Provider<I> getProvider(I id) throws StorageEngineException;

    /**
     * @return all known providers
     * @throws StorageEngineException
     */
    BlockingQueue<Provider<I>> getAllProviders() throws StorageEngineException;

    /**
     * @param provider
     * @param recursive
     * @return IDs for records for this provider
     * @throws StorageEngineException
     */
    BlockingQueue<I> getMetaDataRecordIdsByProvider(Provider<I> provider, boolean recursive)
            throws StorageEngineException;

    /**
     * @param provider
     * @param recursive
     * @return IDs for records for this provider
     * @throws StorageEngineException
     */
    BlockingQueue<MetaDataRecord<I>> getMetaDataRecordsByProvider(Provider<I> provider, boolean recursive)
            throws StorageEngineException;
}
