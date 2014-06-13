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
import java.util.List;

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
            @QueryParam(UimParamConstants.NAME) String name, @QueryParam(UimParamConstants.PARAMETER) List<String> parameters) {
//            throws DatabaseConnectionException, RecordExistsException, ProviderDoesNotExistException,
//            RecordDatasetEmptyException, CloudIdDoesNotExistException 
//
//        return localId != null ? Response.ok().entity(uniqueIdentifierService.createCloudId(providerId, localId))
//                .build() : Response.ok().entity(uniqueIdentifierService.createCloudId(providerId)).build();
        return null;
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
