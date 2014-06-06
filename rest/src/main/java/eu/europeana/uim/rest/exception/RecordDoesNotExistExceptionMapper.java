package eu.europeana.uim.rest.exception;

import eu.europeana.cloud.service.uis.exception.RecordDoesNotExistException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * RecordDoesNotExist exception mapper
 * 
 * @author Yorgos.Mamakis@ kb.nl
 * @since Dec 17, 2013
 */
@Provider
public class RecordDoesNotExistExceptionMapper extends UISExceptionMapper implements
		ExceptionMapper<RecordDoesNotExistException> {

}
