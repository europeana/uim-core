package org.theeuropeanlibrary.uim.gui.gwt.client;

import java.util.List;
import java.util.Set;

import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ParameterDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.StepStatusDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Definition of the asynchronous service to retrieve orchestration dependent information from the
 * server.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
public interface OrchestrationServiceAsync {
    /**
     * Delivers registered workflows
     * 
     * @param async
     */
    void getWorkflows(AsyncCallback<List<WorkflowDTO>> async);

    /**
     * Returns status of the given workflow
     * 
     * @param workflow
     * @param async
     */
    void getStatus(String workflow, AsyncCallback<List<StepStatusDTO>> async);

    /**
     * Returns a list of all known provider for a given provider
     * 
     * @param async
     */
    void getProviders(AsyncCallback<List<ProviderDTO>> async);

    /**
     * Returns a list of all known collection for a given provider
     * 
     * @param collection
     * @param async
     */
    void getCollections(Long collection, AsyncCallback<List<CollectionDTO>> async);

    /**
     * Returns number of metadata records in the provided collection
     * 
     * @param collection
     * @param async
     */
    void getCollectionTotal(Long collection, AsyncCallback<Integer> async);

    /**
     * Returns list of stored parameters queried from the resource engine specific to a workflow
     * alone or together with a provider or collection
     * 
     * @param provider
     *            null or provider specific parameter
     * @param collection
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @param async
     */
    void getParameters(Long provider, Long collection, String workflow,
            AsyncCallback<List<ParameterDTO>> async);

    /**
     * Set the given parameter in the resource engine specific to a workflow alone or together with
     * a provider or collection
     * 
     * @param parameter
     *            should not be null, values can be null (delete) or an empty array list (defined as
     *            empty) or the values
     * @param provider
     *            null or provider specific parameter
     * @param collection
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @param async
     */
    void setParameters(ParameterDTO parameter, Long provider, Long collection, String workflow,
            AsyncCallback<Boolean> async);

    /**
     * Returns list of resources file names in the configured resource directory
     * 
     * @param async
     */
    void getResourceFileNames(AsyncCallback<List<String>> async);

    /**
     * Triggers a new execution.
     * 
     * @param workflow
     *            what should be done?
     * @param collection
     *            on what data?
     * @param executionName
     *            name for execution
     * @param parameters
     *            execution specific parameters which override potentially configured ones
     * @param async
     */
    void startCollection(String workflow, Long collection, String executionName,
            Set<ParameterDTO> parameters, AsyncCallback<ExecutionDTO> async);

    /**
     * Triggers a new execution.
     * 
     * @param workflow
     *            what should be done?
     * @param provider
     *            on what data?
     * @param executionName
     *            name for execution
     * @param parameters
     *            execution specific parameters which override potentially configured ones
     * @param async
     */
    void startProvider(String workflow, Long provider, String executionName,
            Set<ParameterDTO> parameters, AsyncCallback<ExecutionDTO> async);

    /**
     * Returns list of current running executions
     * 
     * @param id
     * @param async
     */
    void getExecution(Long id, AsyncCallback<ExecutionDTO> async);

    /**
     * Returns list of current running executions
     * 
     * @param async
     */
    void getActiveExecutions(AsyncCallback<List<ExecutionDTO>> async);

    /**
     * Returns list of completed executions
     * 
     * @param async
     */
    void getPastExecutions(AsyncCallback<List<ExecutionDTO>> async);

    /**
     * Pause the given execution (if it is paused or not running, nothing happens and false is
     * returned).
     * 
     * @param execution
     * @param async
     */
    void pauseExecution(Long execution, AsyncCallback<Boolean> async);

    /**
     * Resumes the given execution (if it is not paused, nothing happens and false is returned).
     * 
     * @param execution
     * @param async
     */
    void resumeExecution(Long execution, AsyncCallback<Boolean> async);

    /**
     * Cancels the given execution (if it is not running,, nothing happens and false is returned).
     * 
     * @param execution
     * @param async
     */
    void cancelExecution(Long execution, AsyncCallback<Boolean> async);
}
