package eu.europeana.uim.storage;

import eu.europeana.uim.storage.modules.CollectionStorageEngine;
import eu.europeana.uim.storage.modules.CommandStorageEngine;
import eu.europeana.uim.storage.modules.ConfigurationStorageEngine;
import eu.europeana.uim.storage.modules.ExecutionStorageEngine;
import eu.europeana.uim.storage.modules.IdentifierStorageEngine;
import eu.europeana.uim.storage.modules.MetaDataRecordStorageEngine;
import eu.europeana.uim.storage.modules.ProviderStorageEngine;
import eu.europeana.uim.storage.modules.RequestStorageEngine;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface StorageEngine<I> extends ConfigurationStorageEngine<I>, 
        IdentifierStorageEngine<I>, 
        ProviderStorageEngine<I>, 
        CollectionStorageEngine<I>, 
        RequestStorageEngine<I>, 
        ExecutionStorageEngine<I>,  
        MetaDataRecordStorageEngine<I>,
        CommandStorageEngine<I> {

    /**
     * @return identifier of the storage engine
     */
    String getIdentifier();
}
