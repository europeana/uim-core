package eu.europeana.uim.rest.exception;

import eu.europeana.cloud.service.uis.exception.CloudIdDoesNotExistException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * CloudIdDoesNotExist exception mapper
 * 
 * @author Yorgos.Mamakis@ kb.nl
 * @since Dec 17, 2013
 */
@Provider
public class CloudIdDoesNotExistExceptionMapper extends UISExceptionMapper implements
		ExceptionMapper<CloudIdDoesNotExistException> {

}
