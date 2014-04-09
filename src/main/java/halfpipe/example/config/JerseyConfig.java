package halfpipe.example.config;

import halfpipe.example.endpoint.HelloEndpoint;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

//@ApplicationPath("/v1.0")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //TODO: get packages from spring
        packages(HelloEndpoint.class.getPackage().getName());
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.JSON_PROCESSING_FEATURE_DISABLE, false);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        register(RequestContextFilter.class);
        register(LoggingFilter.class);
        register(JacksonFeature.class);
    }
}