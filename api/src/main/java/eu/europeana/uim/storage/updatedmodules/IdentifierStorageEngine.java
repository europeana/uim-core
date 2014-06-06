/* IdentifierStorageEngine.java - created on Apr 25, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.storage.updatedmodules;

import eu.europeana.uim.store.UimEntity;
import java.util.List;

/**
 * Identifier class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 25, 2014
 */
public interface IdentifierStorageEngine<I> {

//    /**
//     * @param external ids
//     * @return storage engine based ids
//     */
//    List<I> getUimId(String... externalId);
//    
        /**
     * @param entityClass
     * @param external ids
     * @return storage engine based ids
     */
    I getUimId(Class<? extends UimEntity> entityClass, String externalId);
    
            /**
     * @param entityClass
     * @param external ids
     * @return storage engine based ids
     */
    List<I> getUimIds(Class<? extends UimEntity> entityClass, String... externalId);

//    /**
//     * @param createIfMissing
//     * @param parent entity representing parent
//     * @param external external identifiers to retrieve internal IDs
//     * @return storage engine based ids
//     */
//    List<I> getStorageId(boolean createIfMissing, UimEntity<I> parent, String... externalId);
//
//    /**
//     * @param createIfMissing
//     * @param external ids
//     * @return storage engine based ids
//     */
//    List<I> getStorageId(boolean createIfMissing, String... externalId);
//    /**
//     * @param createIfMissing
//     * @param external ids
//     * @return storage engine based ids
//     */
//    List<I> getOrCreateStorageId(String... externalId);
//    
//    /**
//     * @param external
//     * @return storage engine based ids
//     */
//    List<I> getProviderId(String... providerExternalId);
//
//    /**
//     * @param external
//     * @return storage engine based ids
//     */
//    List<I> getCollectionId(Provider<I> provider, String... collectionExternalId);
//
//    /**
//     * @param external
//     * @return storage engine based ids
//     */
//    List<I> getRequestId(Collection<I> collection, String... requestExternalId);
//
//    /**
//     * @param external
//     * @return storage engine based ids
//     */
//    List<I> getExecutionId(String... executionExternalId);
//
//    /**
//     * @param external
//     * @return storage engine based ids
//     */
//    List<I> getMetaDataRecordId(Collection<I> collection, String... recordExternalId);
}
