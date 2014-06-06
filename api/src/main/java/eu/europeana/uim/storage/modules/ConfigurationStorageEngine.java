package eu.europeana.uim.storage.modules;

import eu.europeana.uim.storage.*;
import java.util.Map;

import eu.europeana.uim.EngineStatus;
import eu.europeana.uim.storage.updatedmodules.CollectionStorageEngine;
import eu.europeana.uim.storage.updatedmodules.ExecutionStorageEngine;
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
public interface ConfigurationStorageEngine<I> {

    /**
     * @param config arbitrary key - value map
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
     * Shutdown the engine and its connected components like connection to
     * database.
     */
    void shutdown();

    /**
     * Mark a checkpoint.
     */
    void checkpoint();

    /**
     * @return status of the engine (starting, ...)
     */
    EngineStatus getStatus();
}
