package eu.europeana.uim.plugin.source;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ExecutionPlugin;
import eu.europeana.uim.store.UimDataSet;

/**
 * Start in a UIM workflow. We use this in order to implement the command
 * pattern for workflow execution. The start step is an emphasized step which
 * has additional functionality.
 *
 * @param <U> uim data set type
 * @param <I> generic identifier
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 19, 2012
 */
public interface WorkflowStart<U extends UimDataSet<I>, I> extends ExecutionPlugin<U, I> {

    /**
     * key on execution to keep track of {@link UimDataSet} created during this
     * workflow start (not mandatory, only selected {@link WorkflowStart}s might
     * support this)
     */
    static final String CREATED = "Created";
    /**
     * key on execution to keep track of {@link UimDataSet} updated (existed
     * previously) during this workflow start (not mandatory, only selected
     * {@link WorkflowStart}s might support this)
     */
    static final String UPDATED = "Updated";
    /**
     * key on execution to keep track of {@link UimDataSet} deleted (must not
     * have been existed in UIM before, just marks records as deleted by the
     * provider) during this workflow start (not mandatory, only selected
     * {@link WorkflowStart}s might support this)
     */
    static final String DELETED = "Deleted";

    /**
     * Create a runnable which is executed within a thread pool. The runnable is
     * then scheduled and executed in another thread
     *
     * @param context holds execution depending, information the
     * {@link ExecutionContext} for this processing call. This context can
     * change for each call, so references to it have to be handled carefully.
     * @return runnable which runs in a thread pool executor and loads data
     * @throws WorkflowStartFailedException is thrown if the creation of the
     * task loader failed
     */
    TaskCreator<U, I> createLoader(ExecutionContext<U, I> context)
            throws WorkflowStartFailedException;

    /**
     * Check wheter there is more work to do or not. Finished means, that no new
     * tasks can be created.
     *
     * @param context holds execution depending, information the
     * {@link ExecutionContext} for this processing call. This context can
     * change for each call, so references to it have to be handled carefully.
     * @return true iff no more tasks can be created.
     */
    boolean isFinished(ExecutionContext<U, I> context);

    /**
     * Get the number of total records, if it is known upfront. If not, returns
     * -1.
     *
     * @param context
     * @return the total number of records to process.
     */
    int getTotalSize(ExecutionContext<U, I> context);
}
