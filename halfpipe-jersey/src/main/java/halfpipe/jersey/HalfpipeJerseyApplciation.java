package halfpipe.jersey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import halfpipe.jackson.ObjectMapperProvider;
import halfpipe.logging.Log;
import halfpipe.properties.HalfpipeProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.server.mvc.MvcFeature;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HalfpipeJerseyApplciation extends ResourceConfig {
    private static final String NEWLINE = String.format("%n");

    @Log
    Logger log;

    @Inject
    ApplicationContext context;

    @Inject
    HalfpipeProperties properties;

    @Inject
    ObjectMapperProvider objectMapperProvider;

    public HalfpipeJerseyApplciation() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.JSON_PROCESSING_FEATURE_DISABLE, false);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        register(LoggingFilter.class);
        register(MvcFeature.class);
        register(FreemarkerMvcFeature.class);
        //register(OptionalResourceMethodDispatchAdapter.class);
        //register(OptionalQueryParamValueFactoryProvider.OptionalParamConverterProvider.class);
        //register(OptionalQueryParamValueFactoryProvider.Binder.class);
    }

    @PostConstruct
    public void init() {
        register(objectMapperProvider);

        Map<String, Object> resources = context.getBeansWithAnnotation(Path.class);
        List<String> beanPackages = new ArrayList<>();
        for (Object bean: resources.values()) {
            beanPackages.add(bean.getClass().getPackage().getName());
        }
        packages(beanPackages.toArray(new String[0]));

        logResources(resources.values());
        logProviders();
        logEndpoints(resources.values());
    }
    private void logResources(Collection<Object> resources) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();

        for (Object resource: resources) {
            builder.add(resource.getClass().getCanonicalName());
        }

        for (Object o : getSingletons()) {
            if (o.getClass().isAnnotationPresent(Path.class)) {
                builder.add(o.getClass().getCanonicalName());
            }
        }

        log.debug("resources = {}", builder.build());
    }

    private void logProviders() {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        Map<String, Object> providers = context.getBeansWithAnnotation(Path.class);

        for (Object provider: providers.values()) {
            builder.add(provider.getClass().getCanonicalName());
        }

        for (Object o : getSingletons()) {
            if (o.getClass().isAnnotationPresent(Provider.class)) {
                builder.add(o.getClass().getCanonicalName());
            }
        }

        log.debug("providers = {}", builder.build());
    }

    private void logEndpoints(Collection<Object> resources) {
        final StringBuilder msg = new StringBuilder(1024);
        msg.append("The following paths were found for the configured resources:");
        msg.append(NEWLINE).append(NEWLINE);

        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        for (Object o : getSingletons()) {
            if (o.getClass().isAnnotationPresent(Path.class)) {
                builder.add(o.getClass());
            }
        }
        for (Object resource: resources) {
            builder.add(resource.getClass());
        }

        String rootPath = properties.getPrefix();
        if (rootPath.endsWith("/*")) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }

        for (Class<?> klass : builder.build()) {
            final List<String> endpoints = Lists.newArrayList();
            populateEndpoints(endpoints, rootPath, klass, false);

            for (String line : Ordering.natural().sortedCopy(endpoints)) {
                msg.append(line).append(NEWLINE);
            }
        }

        log.info(msg.toString());
    }

    private void populateEndpoints(List<String> endpoints, String basePath, Class<?> klass,
                                   boolean isLocator) {
        populateEndpoints(endpoints, basePath, klass, isLocator, Resource.builder(klass).build());
    }

    private void populateEndpoints(List<String> endpoints, String basePath, Class<?> klass,
                                   boolean isLocator, Resource resource) {
        if (!isLocator) {
            basePath = normalizePath(basePath, resource.getPath());
        }

        for (ResourceMethod method : resource.getResourceMethods()) {
            endpoints.add(formatEndpoint(method.getHttpMethod(), basePath, klass));
        }

        /*for (AbstractSubResourceMethod method : resource.getSubResourceMethods()) {
            final String path = normalizePath(basePath, method.getPath().getValue());
            endpoints.add(formatEndpoint(method.getHttpMethod(), path, klass));
        }

        for (AbstractSubResourceLocator locator : resource.getSubResourceLocators()) {
            final String path = normalizePath(basePath, locator.getPath().getValue());
            populateEndpoints(endpoints, path, locator.getMethod().getReturnType(), true);
        }*/
    }

    private String formatEndpoint(String method, String path, Class<?> klass) {
        return String.format("    %-7s %s (%s)", method, path, klass.getCanonicalName());
    }

    private String normalizePath(String basePath, String path) {
        if (basePath.endsWith("/")) {
            return path.startsWith("/") ? basePath + path.substring(1) : basePath + path;
        }
        return path.startsWith("/") ? basePath + path : basePath + "/" + path;
    }
}