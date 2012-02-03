package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
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
     * @param providerId
     * @return a list of all known collection for a given provider
     */
    List<CollectionDTO> getCollections(Serializable providerId);

    /**
     * @param collectionId
     * @return number of metadata records in the provided collection
     */
    Integer getCollectionTotal(Serializable collectionId);

    /**
     * Update UIM provider using data from control panel.
     * 
     * @param provider
     * @return true, if update of provider was successfull
     */
    Boolean updateProvider(ProviderDTO provider);

    /**
     * Update UIM collection using data from control panel.
     * 
     * @param collection
     * @return true, if update of collectiono was successfull
     */
    Boolean updateCollection(CollectionDTO collection);

    /**
     * Update UIM provider using data from control panel.
     * 
     * @param providerId
     *            identifier of provider in UIM
     * @return not null, if update of repox provider was successfull (null, also if there is no
     *         configured repox)
     */
    ProviderDTO synchronizeRepoxProvider(Serializable providerId);

    /**
     * Update repox provider using data
     * 
     * @param collectionId
     *            identifier of collection in UIm
     * @return not null, if update of repox collection was successfull (null, also if there is no
     *         configured repox)
     */
    CollectionDTO synchronizeRepoxCollection(Serializable collectionId);

    /**
     * @return true, if update of repox provider was successfull (false, also if there is no
     *         configured repox)
     */
    Boolean synchronizeRepox();
}
