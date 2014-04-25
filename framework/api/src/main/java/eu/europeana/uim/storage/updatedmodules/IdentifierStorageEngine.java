/* IdentifierStorageEngine.java - created on Apr 25, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.storage.updatedmodules;

import java.util.List;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Identifier class for storage engine typed with a ID class.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 25, 2014
 */
public interface IdentifierStorageEngine<I> {
    /**
     * @param external
     * @return storage engine based ids
     */
    List<I> getProviderId(String... providerExternalId);
    
    /**
     * @param external
     * @return storage engine based ids
     */
    List<I> getCollectionId(Provider<I> provider, String... collectionExternalId);
    

    /**
     * @param external
     * @return storage engine based ids
     */
    List<I> getRequestId(Collection<I> collection, String... requestExternalId);

    /**
     * @param external
     * @return storage engine based ids
     */
    List<I> getExecutionId(String... collectionExternalId);

    /**
     * @param external
     * @return storage engine based ids
     */
    List<I> getMetaDataRecordId(Collection<I> collection, String... recordExternalId);
}
