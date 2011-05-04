package org.theeuropeanlibrary.uim.gui.gwt.client;

import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.StepStatusDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service to get the available workflows and so on
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@RemoteServiceRelativePath("orchestrator")
public interface OrchestrationService extends RemoteService {
    List<WorkflowDTO> getWorkflows();

    List<ProviderDTO> getProviders();

    List<CollectionDTO> getCollections(Long provider);

    List<CollectionDTO> getAllCollections();

    ExecutionDTO startCollection(String workflow, Long collection);

    ExecutionDTO startProvider(String workflow, Long provider);

    ExecutionDTO getExecution(Long id);

    List<ExecutionDTO> getActiveExecutions();

    List<ExecutionDTO> getPastExecutions();

    Integer getCollectionTotal(Long collection);

    List<StepStatusDTO> getStatus(String workflow);
}
