package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.europeana.uim.gui.cp.shared.ExecutionDTO;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * Definition of the service to retrieve orchestration dependent information from the server.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
@RemoteServiceRelativePath("execution")
public interface ExecutionService extends RemoteService {
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
     * @return created execution
     */
    Boolean startCollection(String workflow, Serializable collectionId, String executionName,
            Set<ParameterDTO> parameters);

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
     * @return created execution
     */
    ExecutionDTO startProvider(String workflow, Serializable providerId, String executionName,
            Set<ParameterDTO> parameters);

    /**
     * @param id
     * @return null or the queried execution (either be it active or an old stored one)
     */
    ExecutionDTO getExecution(Serializable id);

    /**
     * @return list of current running executions
     */
    List<ExecutionDTO> getActiveExecutions();

    /**
     * @return list of completed executions
     */
    List<ExecutionDTO> getPastExecutions();

    /**
     * @param workflows
     *            the list of workflows to be considered. Null means all.
     * @return list of completed executions from a list of specific workflows
     */
    List<ExecutionDTO> getPastExecutions(String[] workflows);

    /**
     * @param workflows  the list of workflows to be considered. Null means all.
     * @param collmenmonic filter executions for a specific collection
     * @param start filter executions after the given date
     * @param end   filter executions before the given date
     * @return list of completed executions for these criteria
     */
    List<ExecutionDTO> getPastExecutions(String[] workflows,String collmenmonic,Date start, Date end);
    
    
    /**
     * Pause the given execution (if it is paused or not running, nothing happens and false is
     * returned).
     * 
     * @param executionId
     * @return true, if pausing was successfull
     */
    Boolean pauseExecution(Serializable executionId);

    /**
     * Resumes the given execution (if it is not paused, nothing happens and false is returned).
     * 
     * @param executionId
     * @return true, if resuming was successfull
     */
    Boolean resumeExecution(Serializable executionId);

    /**
     * Cancels the given execution (if it is not running,, nothing happens and false is returned).
     * 
     * @param executionId
     * @return true, if cancellation was successfull
     */
    Boolean cancelExecution(Serializable executionId);
}
