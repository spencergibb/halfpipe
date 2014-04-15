package halfpipe.jersey;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.mvc.MvcFeature;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HalfpipeResourceConfig extends ResourceConfig {

    @Inject
    ApplicationContext context;

    public HalfpipeResourceConfig() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.JSON_PROCESSING_FEATURE_DISABLE, false);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        register(RequestContextFilter.class);
        register(LoggingFilter.class);
        register(MvcFeature.class);
        register(FreemarkerMvcFeature.class);
        //register(JacksonFeature.class);
    }

    @PostConstruct
    public void init() {
        Map<String, Object> resources = context.getBeansWithAnnotation(Path.class);
        List<String> beanPackages = new ArrayList<>();
        for (Object bean: resources.values()) {
            beanPackages.add(bean.getClass().getPackage().getName());
        }
        packages(beanPackages.toArray(new String[0]));
    }
}