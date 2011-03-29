package eu.europeana.uim.gui.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.europeana.uim.gui.gwt.shared.CollectionDTO;
import eu.europeana.uim.gui.gwt.shared.ExecutionDTO;
import eu.europeana.uim.gui.gwt.shared.ProviderDTO;
import eu.europeana.uim.gui.gwt.shared.StepStatusDTO;
import eu.europeana.uim.gui.gwt.shared.WorkflowDTO;

public interface OrchestrationServiceAsync {
    void getWorkflows(AsyncCallback<List<WorkflowDTO>> async);

    void getProviders(AsyncCallback<List<ProviderDTO>> async);

    void getCollections(Long provider, AsyncCallback<List<CollectionDTO>> async);

    void getAllCollections(AsyncCallback<List<CollectionDTO>> async);

    void startCollection(String workflow, Long collection, AsyncCallback<ExecutionDTO> async);

    void startProvider(String workflow, Long provider, AsyncCallback<ExecutionDTO> async);

    void getExecution(Long id, AsyncCallback<ExecutionDTO> async);

    void getCollectionTotal(Long collection, AsyncCallback<Integer> async);

    void getActiveExecutions(AsyncCallback<List<ExecutionDTO>> async);

    void getPastExecutions(AsyncCallback<List<ExecutionDTO>> async);

    void getStatus(String workflow, AsyncCallback<List<StepStatusDTO>> async);
}
