package eu.europeana.uim.gui.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.europeana.uim.gui.gwt.shared.Collection;
import eu.europeana.uim.gui.gwt.shared.Execution;
import eu.europeana.uim.gui.gwt.shared.Provider;
import eu.europeana.uim.gui.gwt.shared.StepStatus;
import eu.europeana.uim.gui.gwt.shared.Workflow;

/**
 * Service to get the available workflows and so on
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@RemoteServiceRelativePath("orchestrator")
public interface OrchestrationService extends RemoteService {

    List<Workflow> getWorkflows();

    List<Provider> getProviders();

    List<Collection> getCollections(Long provider);

    List<Collection> getAllCollections();

    Execution startCollection(String workflow, Long collection);

    Execution startProvider(String workflow, Long provider);

    Execution getExecution(Long id);

    List<Execution> getActiveExecutions();

    List<Execution> getPastExecutions();

    Integer getCollectionTotal(Long collection);

    List<StepStatus> getStatus(String workflow);






}
