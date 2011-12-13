package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.europeana.uim.gui.cp.shared.ExecutionDTO;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * Definition of the asynchronous service to retrieve orchestration dependent information from the
 * server.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
public interface ExecutionServiceAsync {
    /**
     * Triggers a new execution.
     * 
     * @param workflow
     *            what should be done?
     * @param collectionId
     *            on what data?
     * @param executionName
     *            name for execution
     * @param parameters
     *            execution specific parameters which override potentially configured ones
     * @param async
     */
    void startCollection(String workflow, Serializable collectionId, String executionName,
            Set<ParameterDTO> parameters, AsyncCallback<ExecutionDTO> async);

    /**
     * Triggers a new execution.
     * 
     * @param workflow
     *            what should be done?
     * @param providerId
     *            on what data?
     * @param executionName
     *            name for execution
     * @param parameters
     *            execution specific parameters which override potentially configured ones
     * @param async
     */
    void startProvider(String workflow, Serializable providerId, String executionName,
            Set<ParameterDTO> parameters, AsyncCallback<ExecutionDTO> async);

    /**
     * Returns list of current running executions
     * 
     * @param id
     * @param async
     */
    void getExecution(Serializable id, AsyncCallback<ExecutionDTO> async);

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
     * Returns list of completed executions for a list of workflows
     * 
     * @param workflows
     *            the list of workflows, null means all.
     * @param async
     */
    void getPastExecutions(String[] workflows, AsyncCallback<List<ExecutionDTO>> async);

    /**
     * Pause the given execution (if it is paused or not running, nothing happens and false is
     * returned).
     * 
     * @param executionId
     * @param async
     */
    void pauseExecution(Serializable executionId, AsyncCallback<Boolean> async);

    /**
     * Resumes the given execution (if it is not paused, nothing happens and false is returned).
     * 
     * @param executionId
     * @param async
     */
    void resumeExecution(Serializable executionId, AsyncCallback<Boolean> async);

    /**
     * Cancels the given execution (if it is not running,, nothing happens and false is returned).
     * 
     * @param executionId
     * @param async
     */
    void cancelExecution(Serializable executionId, AsyncCallback<Boolean> async);
}
