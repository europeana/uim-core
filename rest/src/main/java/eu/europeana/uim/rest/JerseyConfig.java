package eu.europeana.uim.rest;

import eu.europeana.uim.rest.exception.StorageEngineExceptionMapper;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Jersey Configuration for Exception Mappers and Resources
 *
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Mar 24, 2014
 */
public class JerseyConfig extends ResourceConfig {

    /**
     * Creates a new instance of this class.
     */
    public JerseyConfig() {
        super();
        register(RequestContextFilter.class);
        register(LoggingFilter.class);
        register(StorageEngineExceptionMapper.class);
        register(ExecutionResource.class);
        register(ExecutionsResource.class);
    }
}
