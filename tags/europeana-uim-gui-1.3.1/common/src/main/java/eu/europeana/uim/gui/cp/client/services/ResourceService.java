package eu.europeana.uim.gui.cp.client.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * Definition of the service to retrieve resource dependent information from the server.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 23, 2011
 */
@RemoteServiceRelativePath("resource")
public interface ResourceService extends RemoteService {
    /**
     * @param providerId
     *            null or provider specific parameter
     * @param collectionId
     *            null or collection specific parameter
     * @param workflow
     *            cannot be null defines parameter set
     * @return list of stored parameters queried from the resource engine specific to a workflow
     *         alone or together with a provider or collection
     */
    List<ParameterDTO> getParameters(Serializable providerId, Serializable collectionId, String workflow);

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
     * @return true, if changing was successfull
     */
    Boolean setParameters(ParameterDTO parameter, Serializable providerId, Serializable collectionId, String workflow);

    /**
     * @return list of resources file names in the configured resource directory
     */
    List<String> getResourceFileNames();
}
