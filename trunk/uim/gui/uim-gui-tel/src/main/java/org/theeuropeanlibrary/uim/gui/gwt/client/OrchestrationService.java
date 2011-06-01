package org.theeuropeanlibrary.uim.gui.gwt.client;

import java.util.List;
import java.util.Set;

import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.MetaDataRecordDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ParameterDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.SearchResultDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.StepStatusDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Definition of the service to retrieve orchestration dependent information from the server.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
@RemoteServiceRelativePath("orchestrator")
public interface OrchestrationService extends RemoteService {
    /**
     * @return delivers registered workflows
     */
    List<WorkflowDTO> getWorkflows();

    /**
     * @param workflow
     * @return status of the given workflow
     */
    List<StepStatusDTO> getStatus(String workflow);

    /**
     * @return a list of all stored provider
     */
    List<ProviderDTO> getProviders();

    /**
     * @param provider
     * @return a list of all known collection for a given provider
     */
    List<CollectionDTO> getCollections(Long provider);

    /**
     * @param collection
     * @return number of metadata records in the provided collection
     */
    Integer getCollectionTotal(Long collection);

    /**
     * @param provider
     *            null or provider specific parameter
     * @param collection
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @return list of stored parameters queried from the resource engine specific to a workflow
     *         alone or together with a provider or collection
     */
    List<ParameterDTO> getParameters(Long provider, Long collection, String workflow);

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
     * @return true, if changing was successfull
     */
    Boolean setParameters(ParameterDTO parameter, Long provider, Long collection, String workflow);

    /**
     * @return list of resources file names in the configured resource directory
     */
    List<String> getResourceFileNames();

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
     * @return created execution
     */
    ExecutionDTO startCollection(String workflow, Long collection, String executionName,
            Set<ParameterDTO> parameters);

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
     * @return created execution
     */
    ExecutionDTO startProvider(String workflow, Long provider, String executionName,
            Set<ParameterDTO> parameters);

    /**
     * @param id
     * @return null or the queried execution (either be it active or an old stored one)
     */
    ExecutionDTO getExecution(Long id);

    /**
     * @return list of current running executions
     */
    List<ExecutionDTO> getActiveExecutions();

    /**
     * @return list of completed executions
     */
    List<ExecutionDTO> getPastExecutions();

    /**
     * Pause the given execution (if it is paused or not running, nothing happens and false is
     * returned).
     * 
     * @param execution
     * @return true, if pausing was successfull
     */
    Boolean pauseExecution(Long execution);

    /**
     * Resumes the given execution (if it is not paused, nothing happens and false is returned).
     * 
     * @param execution
     * @return true, if resuming was successfull
     */
    Boolean resumeExecution(Long execution);

    /**
     * Cancels the given execution (if it is not running,, nothing happens and false is returned).
     * 
     * @param execution
     * @return true, if cancellation was successfull
     */
    Boolean cancelExecution(Long execution);

    /**
     * Delivers up to a maxSize number of records starting at the offset for the provided collection
     * from the central repository.
     * 
     * @param collection
     * @param maxSize
     * @param offset
     * @return list of simple metadata records to be shown in repository browsing view
     */
    List<MetaDataRecordDTO> getRecordsForCollection(Long collection, int offset, int maxSize);

    /**
     * @param recordId
     * @return raw record how we got it in our uim
     */
    String getRawRecord(Long recordId);

    /**
     * @param recordId
     * @return xml representation of our metadata record object model
     */
    String getXmlRecord(Long recordId);

// List<MetaDataRecordDTO> getRecordsForProvider(Long provider, int maxSize, int offset);

    /**
     * Delivers up to a maxSize number of records starting at the offset for the provided collection
     * from the search index together with facets and the total number of results.
     * 
     * @param searchQuery
     * @param offset
     * @param maxSize
     * @param facets
     * @return search result object with e.g. list of simple metadata records to be shown in search
     *         view
     */
    SearchResultDTO searchIndex(String searchQuery, int offset, int maxSize, List<String> facets);

    /**
     * @param recordId
     * @return xml representation of the actual record in the search index
     */
    String getSearchRecord(Long recordId);
// List<SearchRecordDTO> searchNgram(String searchQuery, int maxSize, int offset);
// List<SearchRecordDTO> searchMacs(String searchQuery, int maxSize, int offset);
}
