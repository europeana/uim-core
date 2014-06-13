package eu.europeana.uim.storage.modules;

import java.util.concurrent.BlockingQueue;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
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
    Execution<I> createExecution(UimDataSet<I> dataSet, Workflow workflow)
            throws StorageEngineException;

    /**
     * Stores the given execution and its updated values.
     *
     * @param execution
     * @throws StorageEngineException
     */
    void updateExecution(Execution<I> execution) throws StorageEngineException;

    /**
     * @param id unique ID, unique over collection, provider, ...
     * @return execution under the given ID
     * @throws StorageEngineException
     */
    Execution<I> getExecution(I id) throws StorageEngineException;

    /**
     * @return all known executions
     * @throws StorageEngineException
     */
    BlockingQueue<Execution<I>> getAllExecutions() throws StorageEngineException;

    /**
     * Finalization method (tear down) for an execution. At the end of each
     * execution this method is called to allow the storage engine to clean up
     * memory or external resources.
     *
     * @param context holds execution depending, information the
     * {@link ExecutionContext} for this processing call. This context can
     * change for each call, so references to it have to be handled carefully.
     */
    void completed(ExecutionContext<?, I> context);
}
