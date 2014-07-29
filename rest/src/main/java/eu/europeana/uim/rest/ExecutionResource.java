package eu.europeana.uim.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.qmino.miredot.annotations.ReturnType;

import eu.europeana.uim.Registry;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.Orchestrator;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.workflow.Workflow;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * REST interface for starting, stopping, retrieving etc. of executions.
 *
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Mar 24, 2014
 */
@Component
@Path("/")
@Scope("request")
public class ExecutionResource {

    @Autowired
    private Registry registry;

    private static final String ID = "executionId";

    @PathParam(ID)
    private String executionId;

    @POST
    @Path("execution")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response startExecution(@QueryParam(UimParamConstants.WORKFLOW) String workflow, @QueryParam(UimParamConstants.DATASET_ID) String datasetId,
            @QueryParam(UimParamConstants.NAME) String name, @QueryParam(UimParamConstants.PARAMETER) Map<String, String[]> parameters) throws StorageEngineException {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>) registry.getStorageEngine();
        if (storage == null) {
            throw new StorageEngineException("Storage engine has not been configured!");
        }

        Collection<Serializable> coll = storage.getCollection(datasetId);
        if (coll == null) {
            throw new StorageEngineException("Storage doesn't know element with dataset '" + datasetId + "' with id '" + datasetId + "'!");
        }

        Workflow wf = registry.getWorkflow(
                workflow);

        Orchestrator<Serializable> orchestrator = (Orchestrator<Serializable>) registry.getOrchestrator();

        ActiveExecution<?, Serializable> ae;
        if (parameters != null) {
            Properties properties = prepareProperties(parameters);
            ae = orchestrator.executeWorkflow(wf, coll, properties);
        } else {
            ae = orchestrator.executeWorkflow(wf, coll);
        }

        if (name != null) {
            ae.getExecution().setName(name);
        }
        return Response.ok().entity(ae != null).build();
    }

    private Properties prepareProperties(Map<String, String[]> parameters) {
        Properties properties = new Properties();
        for (Map.Entry<String, String[]> parameter : parameters.entrySet()) {
            if (parameter.getValue() != null) {
                if (parameter.getValue().length > 1) {
                    StringBuilder b = new StringBuilder();
                    for (String val : parameter.getValue()) {
                        b.append(val);
                        b.append(",");
                    }
                    b.deleteCharAt(b.length() - 1);
                    properties.put(parameter.getKey(), b.toString());
                } else if (parameter.getValue().length == 1) {
                    properties.put(parameter.getKey(), parameter.getValue()[0]);
                }
            }
        }
        return properties;
    }

    @GET
    @Path("execution/{" + ID + "}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response getExecution() {
        return null;
    }

    @POST
    @Path("execution/{" + ID + "}/pause")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response pauseExecution() {
//        return Response.ok(uniqueIdentifierService.getCloudId(providerId, recordId)).build();
        return null;
    }

    @POST
    @Path("execution/{" + ID + "}/resume")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response resumeExecution() {
//        return Response.ok(uniqueIdentifierService.getCloudId(providerId, recordId)).build();
        return null;
    }

    @POST
    @Path("execution/{" + ID + "}/cancel")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response cancelExecution() {
//        return Response.ok(uniqueIdentifierService.getCloudId(providerId, recordId)).build();
        return null;
    }
}
