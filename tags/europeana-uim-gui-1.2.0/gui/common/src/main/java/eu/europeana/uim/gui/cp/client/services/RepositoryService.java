package eu.europeana.uim.gui.cp.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;

/**
 * Definition of the service to retrieve orchestration dependent information from the server.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
@RemoteServiceRelativePath("repository")
public interface RepositoryService extends RemoteService {
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
}
