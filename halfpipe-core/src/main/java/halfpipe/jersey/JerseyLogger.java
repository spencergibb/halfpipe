package halfpipe.jersey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.sun.jersey.core.reflection.AnnotatedMethod;
import com.sun.jersey.core.reflection.MethodList;
import halfpipe.configuration.Configuration;
import halfpipe.logging.Log;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/10/13
 * Time: 9:44 AM
 */
@Service
public class JerseyLogger implements ApplicationContextAware {
    private static final Logger LOGGER = Log.forThisClass();

    @Inject
    Configuration config;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void logEndpoints() {
        final StringBuilder stringBuilder = new StringBuilder(1024).append(
                "The following paths were found for the configured resources:\n\n");

        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        Map<String,Object> resources = applicationContext.getBeansWithAnnotation(Path.class);
        for (Object o : resources.values()) {
            if (o.getClass().isAnnotationPresent(Path.class)) {
                builder.add(o.getClass());
            }
        }

        for (Class<?> klass : builder.build()) {
            final String path = klass.getAnnotation(Path.class).value();
            String rootPath = config.http.resourcePattern.get();
            if (rootPath.endsWith("/*")) {
                rootPath = rootPath.substring(0,
                        rootPath.length() - (path.startsWith("/") ? 2 : 1));
            }

            final ImmutableList.Builder<String> endpoints = ImmutableList.builder();
            for (AnnotatedMethod method : annotatedMethods(klass)) {
                final StringBuilder pathBuilder = new StringBuilder()
                        .append(rootPath)
                        .append(path);
                if (method.isAnnotationPresent(Path.class)) {
                    final String methodPath = method.getAnnotation(Path.class).value();
                    if (!methodPath.startsWith("/") && !path.endsWith("/")) {
                        pathBuilder.append('/');
                    }
                    pathBuilder.append(methodPath);
                }
                for (HttpMethod verb : method.getMetaMethodAnnotations(HttpMethod.class)) {
                    endpoints.add(String.format("    %-7s %s (%s)",
                            verb.value(),
                            pathBuilder.toString(),
                            klass.getCanonicalName()));
                }
            }

            for (String line : Ordering.natural().sortedCopy(endpoints.build())) {
                stringBuilder.append(line).append('\n');
            }
        }

        LOGGER.info(stringBuilder.toString());
    }

    private MethodList annotatedMethods(Class<?> resource) {
        return new MethodList(resource, true).hasMetaAnnotation(HttpMethod.class);
    }

}
