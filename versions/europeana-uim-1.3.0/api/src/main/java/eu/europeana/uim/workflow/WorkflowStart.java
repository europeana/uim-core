package eu.europeana.uim.workflow;

import java.util.List;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Start in a UIM workflow. We use this in order to implement the command pattern for workflow
 * execution. The start step is an emphasized step which has additional functionality.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStart {
    /**
     * key on execution to keep track of {@link MetaDataRecord} created during this workflow start
     * (not mandatory, only selected {@link WorkflowStart}s might support this)
     */
    static final String CREATED = "Created Records";
    /**
     * key on execution to keep track of {@link MetaDataRecord} updated (existed previously) during
     * this workflow start (not mandatory, only selected {@link WorkflowStart}s might support this)
     */
    static final String UPDATED = "Updated Records";
    /**
     * key on execution to keep track of {@link MetaDataRecord} deleted (must not have been existed
     * in UIM before, just marks records as deleted by the provider) during this workflow start (not
     * mandatory, only selected {@link WorkflowStart}s might support this)
     */
    static final String DELETED = "Deleted Records";

    /**
     * @return identifier of the workflow start, should be simple classname
     */
    String getIdentifier();

    /**
     * @return name of the workflow, should be reasonable meaningful
     */
    String getName();

    /**
     * Get the description of the plugin which is provided to the operators when starting analyzing
     * workflows.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * List of configuration parameters this plugin can take from the execution context to be
     * configured for a specific execution. NOTE: any execution related configuration/information
     * needs to be stored into the context and retrieved from the context during the processRecord
     * method.
     * 
     * @return list of configuration parameters.
     */
    List<String> getParameters();

    /**
     * A plugin is always executed within a thread pool, this parameter defines the preferred size
     * of the pool. Plugins should know best, what's a good level of parallelism.
     * 
     * @return number of threads this plugin should usually be processed.
     */
    int getPreferredThreadCount();

    /**
     * Number of maximum threads. The plugin might specify here one (1) if it is not thread safe.
     * 
     * @return the number of maximal threads.
     */
    int getMaximumThreadCount();

    /**
     * Create a runnable which is executed within a thread pool. The runnable is then scheduled and
     * executed in another thread
     * 
     * @param <I>
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @param storage
     *            reference to storage
     * @return runnable which runs in a thread pool executor and loads data
     * @throws WorkflowStartFailedException
     *             is thrown if the creation of the task loader failed
     */
    <I> TaskCreator<I> createLoader(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException;

    /**
     * Check wheter there is more work to do or not. Finished means, that no new tasks can be
     * created.
     * 
     * @param <I>
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @param storage
     *            reference to storage
     * @return true iff no more tasks can be created.
     */
    <I> boolean isFinished(ExecutionContext<I> context, StorageEngine<I> storage);

    /**
     * Workflow start specific initialization with storage engine so that mdr's can be read from the
     * engine.
     * 
     * @param <I>
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @param storage
     *            reference to storage
     * @throws WorkflowStartFailedException
     *             is thrown if the intiliazation fails and so the workflow is not ready to create a
     *             task loader
     */
    <I> void initialize(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException;

    /**
     * Finalization method (tear down) for an execution. At the end of each execution this method is
     * called to allow the plugin to clean up memory or external resources.
     * 
     * @param <I>
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @param storage
     *            reference to storage
     * @throws WorkflowStartFailedException
     *             is thrown if the tear down encountered a severe failure during deleting external
     *             resources, so that executing it in a new {@link ExecutionContext} will most
     *             likely fail
     */
    <I> void completed(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException;

    /**
     * Get the number of total records, if it is known upfront. If not, returns -1.
     * 
     * @param <I>
     * 
     * @param context
     * @return the total number of records to process.
     */
    <I> int getTotalSize(ExecutionContext<I> context);
}
