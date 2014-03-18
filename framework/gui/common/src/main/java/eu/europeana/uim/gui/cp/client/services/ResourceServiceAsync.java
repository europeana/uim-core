package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * Definition of the asynchronous service to retrieve orchestration dependent information from the
 * server.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
public interface ResourceServiceAsync {
    /**
     * Returns list of stored parameters queried from the resource engine specific to a workflow
     * alone or together with a provider or collection
     * 
     * @param providerId
     *            null or provider specific parameter
     * @param collectionId
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @param async
     */
    void getParameters(Serializable providerId, Serializable collectionId, String workflow,
            AsyncCallback<List<ParameterDTO>> async);

    /**
     * Set the given parameter in the resource engine specific to a workflow alone or together with
     * a provider or collection
     * 
     * @param parameter
     *            should not be null, values can be null (delete) or an empty array list (defined as
     *            empty) or the values
     * @param providerId
     *            null or provider specific parameter
     * @param collectionId
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @param async
     */
    void setParameters(ParameterDTO parameter, Serializable providerId, Serializable collectionId, String workflow,
            AsyncCallback<Boolean> async);

    /**
     * Returns list of resources file names in the configured resource directory
     * 
     * @param async
     */
    void getResourceFileNames(AsyncCallback<List<String>> async);
}
