package eu.europeana.uim.rest.exception;

import eu.europeana.cloud.service.uis.exception.RecordDatasetEmptyException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * RecordDatasetEmpty exception mapper
 * 
 * @author Yorgos.Mamakis@ kb.nl
 * @since Dec 17, 2013
 */
@Provider
public class RecordDatasetEmptyExceptionMapper extends UISExceptionMapper implements
		ExceptionMapper<RecordDatasetEmptyException> {

}
