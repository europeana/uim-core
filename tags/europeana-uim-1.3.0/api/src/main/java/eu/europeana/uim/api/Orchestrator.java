package eu.europeana.uim.api;

import java.util.Collection;
import java.util.Properties;

import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Orchestrates the ingestion job execution.
 * @param <I> 
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Orchestrator<I> {
    /**
     * @return unique identifier of orchestrator
     */
    public String getIdentifier();

    /**
     * @param workflow
     *            execution on this workflow
     * @param dataset
     *            data set executed in the process
     * @return an newly created active execution
     */
    ActiveExecution<I> executeWorkflow(Workflow workflow, UimDataSet<I> dataset);

    /**
     * @param workflow
     *            execution on this workflow
     * @param dataset
     *            data set executed in the process
     * @param properties
     *            arbitrary additional settings
     * @return an newly created active execution
     */
    ActiveExecution<I> executeWorkflow(Workflow workflow, UimDataSet<I> dataset, Properties properties);

    /**
     * @param id
     *            unique ID
     * @return active execution of given ID
     */
    ActiveExecution<I> getActiveExecution(I id);

    /**
     * @return all active executions
     */
    Collection<ActiveExecution<I>> getActiveExecutions();

    /**
     * @param execution
     *            pause this execution
     */
    void pause(ActiveExecution<I> execution);

    /**
     * @param execution
     *            resume this execution
     */
    void resume(ActiveExecution<I> execution);

    /**
     * @param execution
     *            cancel this execution
     */
    void cancel(ActiveExecution<I> execution);

    /**
     * Shutdown the orchestrator.
     */
    void shutdown();
}
