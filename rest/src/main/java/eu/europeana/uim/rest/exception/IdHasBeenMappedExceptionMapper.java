package eu.europeana.uim.rest.exception;

import eu.europeana.cloud.service.uis.exception.IdHasBeenMappedException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * IdHasBeenMapped exception mapper
 * 
 * @author Yorgos.Mamakis@ kb.nl
 * @since Dec 17, 2013
 */
@Provider
public class IdHasBeenMappedExceptionMapper extends UISExceptionMapper implements
		ExceptionMapper<IdHasBeenMappedException> {

}
