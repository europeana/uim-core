package eu.europeana.uim.rest.exception;

import eu.europeana.uim.storage.StorageEngineException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for storage engine based problems.
 *
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since 13.06.2014
 */
@Provider
public class StorageEngineExceptionMapper implements
        ExceptionMapper<StorageEngineException> {

    @Override
    public Response toResponse(StorageEngineException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
}
