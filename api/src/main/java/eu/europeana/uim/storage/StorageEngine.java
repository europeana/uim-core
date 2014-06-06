package eu.europeana.uim.storage;

import eu.europeana.uim.storage.updatedmodules.CollectionStorageEngine;
import eu.europeana.uim.storage.updatedmodules.CommandStorageEngine;
import eu.europeana.uim.storage.updatedmodules.ConfigurationStorageEngine;
import eu.europeana.uim.storage.updatedmodules.ExecutionStorageEngine;
import eu.europeana.uim.storage.updatedmodules.IdentifierStorageEngine;
import eu.europeana.uim.storage.updatedmodules.MetaDataRecordStorageEngine;
import eu.europeana.uim.storage.updatedmodules.ProviderStorageEngine;
import eu.europeana.uim.storage.updatedmodules.RequestStorageEngine;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface StorageEngine<I> extends ConfigurationStorageEngine<I>, CommandStorageEngine<I>, CollectionStorageEngine<I>, ExecutionStorageEngine<I>,
        MetaDataRecordStorageEngine<I>, ProviderStorageEngine<I>, RequestStorageEngine<I>, IdentifierStorageEngine<I> {

    /**
     * @return identifier of the storage engine
     */
    String getIdentifier();
}
