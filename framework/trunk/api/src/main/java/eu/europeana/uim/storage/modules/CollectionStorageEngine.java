package eu.europeana.uim.storage.modules;

import java.util.List;

import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Base class for storage engine typed with a ID class.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface CollectionStorageEngine<I> {
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
     * @return number of records for this collection
     * @throws StorageEngineException
     */
    int getTotalByCollection(Collection<I> collection) throws StorageEngineException;
    
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
}
