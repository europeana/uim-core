package eu.europeana.uim.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.qmino.miredot.annotations.ReturnType;

import eu.europeana.uim.Registry;

/**
 * REST interface for starting, stopping, retrieving etc. of executions.
 *
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Mar 24, 2014
 */
@Component
@Path("/")
@Scope("request")
public class ExecutionsResource {

    @Autowired
    private Registry registry;

    @GET
    @Path("executions/active")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response getActiveExecutions() {
//        return Response.ok(uniqueIdentifierService.getCloudId(providerId, recordId)).build();
        return null;
    }

    @GET
    @Path("executions/finished")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ReturnType("eu.europeana.cloud.common.model.CloudId")
    public Response getFinishedExecutions(@QueryParam(UimParamConstants.WORKFLOW) String workflow, @QueryParam(UimParamConstants.DATASET_ID) String datasetId,
            @QueryParam(UimParamConstants.START_DATA) String startDate, @QueryParam(UimParamConstants.END_DATA) String endDate) {
//        return Response.ok(uniqueIdentifierService.getCloudId(providerId, recordId)).build();
        return null;
    }
}
