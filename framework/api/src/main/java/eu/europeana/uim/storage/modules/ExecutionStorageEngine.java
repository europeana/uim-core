package eu.europeana.uim.storage.modules;

import java.util.List;

import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Execution;
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
public interface ExecutionStorageEngine<I> {
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
}
