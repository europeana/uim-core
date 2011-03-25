package eu.europeana.uim.api;

import java.util.Collection;
import java.util.Properties;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Orchestrates the ingestion job execution.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 21, 2011
 */
public interface Orchestrator {
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
    ActiveExecution<?> executeWorkflow(Workflow workflow, DataSet<?> dataset);

    /**
     * @param workflow
     *            execution on this workflow
     * @param dataset
     *            data set executed in the process
     * @param properties
     *            arbitrary additional settings
     * @return an newly created active execution
     */
    ActiveExecution<?> executeWorkflow(Workflow workflow, DataSet<?> dataset, Properties properties);

    /**
     * @param <I>
     *            generic ID
     * @param id
     *            unique ID
     * @return active execution of given ID
     */
    <I> ActiveExecution<I> getActiveExecution(I id);

    /**
     * @return all active executions
     */
    Collection<ActiveExecution<?>> getActiveExecutions();

    /**
     * Shutdown the orchestrator.
     */
    void shutdown();
}
