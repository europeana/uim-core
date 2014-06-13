package eu.europeana.uim.rest;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import eu.europeana.uim.rest.exception.CloudIdDoesNotExistExceptionMapper;
import eu.europeana.uim.rest.exception.DatabaseConnectionExceptionMapper;
import eu.europeana.uim.rest.exception.IdHasBeenMappedExceptionMapper;
import eu.europeana.uim.rest.exception.ProviderAlreadyExistsExceptionMapper;
import eu.europeana.uim.rest.exception.ProviderDoesNotExistExceptionMapper;
import eu.europeana.uim.rest.exception.RecordDatasetEmptyExceptionMapper;
import eu.europeana.uim.rest.exception.RecordDoesNotExistExceptionMapper;
import eu.europeana.uim.rest.exception.RecordExistsExceptionMapper;
import eu.europeana.uim.rest.exception.RecordIdDoesNotExistExceptionMapper;

/**
 * Jersey Configuration for Exception Mappers and Resources
 *
 * @author Yorgos.Mamakis@ kb.nl
 * @since Dec 17, 2013
 */
public class JerseyConfig extends ResourceConfig {

    /**
     * Creates a new instance of this class.
     */
    public JerseyConfig() {
        super();
        register(RequestContextFilter.class);
        register(LoggingFilter.class);
        register(CloudIdDoesNotExistExceptionMapper.class);
        register(DatabaseConnectionExceptionMapper.class);
        register(IdHasBeenMappedExceptionMapper.class);
        register(ProviderDoesNotExistExceptionMapper.class);
        register(RecordDatasetEmptyExceptionMapper.class);
        register(RecordDoesNotExistExceptionMapper.class);
        register(RecordExistsExceptionMapper.class);
        register(RecordIdDoesNotExistExceptionMapper.class);
        register(ProviderAlreadyExistsExceptionMapper.class);
        register(BasicUniqueIdResource.class);
        register(DataProviderResource.class);
        register(DataProvidersResource.class);
        
        register(ExecutionResource.class);
        register(ExecutionsResource.class);
    }
}
