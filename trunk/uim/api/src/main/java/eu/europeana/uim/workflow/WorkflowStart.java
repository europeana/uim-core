package eu.europeana.uim.workflow;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;

/**
 * Start in a UIM workflow. We use this in order to implement the command pattern for workflow
 * execution. The start step is an emphasized step which has additional functionality.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStart extends IngestionPlugin {

    /**
     * Create a runnable which is executed within a thread pool. The runnable is then scheduled and
     * executed in another thread
     *
     * @param context 
     * @param storage 
     * 
     * @return runnable which runs in a thread pool executor and loads data
     */
     Runnable createLoader(ExecutionContext context, StorageEngine storage);

    /**
     * Create the tasks (@see {@link Task}) which are then processed through the workflow and passed
     * on from step to step.
     * @param context 
     * @param storage 
     * 
     * @return number of tasks created.
     */
     Task[] createWorkflowTasks(ExecutionContext context, StorageEngine storage);

    /**
     * Check wheater there is more work to do or not. Finished means, that no new tasks can be
     * created.
     * 
     * @param context 
     * @param storage 
     * 
     * @return true iff no more tasks can be created.
     */
    boolean isFinished(ExecutionContext context, StorageEngine storage);

    
    /** Workflow start specific initialization with sotrage engine so that mdr's can be read from
     * the engine.
     * 
     * @param context
     * @param storage
     * @throws StorageEngineException
     */
    void initialize(ExecutionContext context, StorageEngine storage) throws StorageEngineException;

    /**
     * Get the number of total records, if it is known upfront. If not, returns -1.
     * 
     * @param context
     * @return the total number of records to process.
     */
    public int getTotalSize(ExecutionContext context);

}
