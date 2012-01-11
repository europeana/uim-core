package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ObjectIdDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;

/**
 * Definition of the asynchronous service to retrieve orchestration dependent information from the
 * server.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
public interface RepositoryServiceAsync {
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
     * @param providerId
     * @param async
     */
    void getCollections(Serializable providerId, AsyncCallback<List<CollectionDTO>> async);

    /**
     * Returns number of metadata records in the provided collection
     * 
     * @param collectionId
     * @param async
     */
    void getCollectionTotal(Serializable collectionId, AsyncCallback<Integer> async);

    /**
     * @param provider
     * @param async
     *            true, if update of provider was successfull
     */
    void updateProvider(ProviderDTO provider, AsyncCallback<Boolean> async);

    /**
     * @param collection
     * @param async
     *            true, if update of collectiono was successfull
     */
    void updateCollection(CollectionDTO collection, AsyncCallback<Boolean> async);

    /**
     * @param obj
     * @param async
     */
    void dummyObjectID(ObjectIdDTO obj, AsyncCallback<Boolean> async);
}
