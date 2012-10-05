package thirtytwo.degrees.halfpipe.config;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.yammer.metrics.HealthChecks;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.HealthCheck;
import com.yammer.metrics.core.HealthCheckRegistry;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.util.DeadlockHealthCheck;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import thirtytwo.degrees.halfpipe.cli.HalfpipeBannerProvider;
import thirtytwo.degrees.halfpipe.jackson.AnnotationSensitivePropertyNamingStrategy;
import thirtytwo.degrees.halfpipe.jackson.DynamicPropertiesModule;
import thirtytwo.degrees.halfpipe.jackson.GuavaExtrasModule;
import thirtytwo.degrees.halfpipe.jackson.ObjectMapperFactory;
import thirtytwo.degrees.halfpipe.jersey.*;

import javax.inject.Named;
import java.util.List;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
public class DefaultAppConfg {

    @Bean @Named(PROP_BANNER_TEXT_FILE)
    public DynamicStringProperty bannerTextFile() {
        return DynamicPropertyFactory.getInstance().getStringProperty(PROP_BANNER_TEXT_FILE, "halfpipebanner.txt");
    }

    @Bean @Named(PROP_APP_NAME)
    public DynamicStringProperty appName() {
        return DynamicPropertyFactory.getInstance().getStringProperty(PROP_APP_NAME, "Halfpipe");
    }

    @Bean
    public HalfpipeBannerProvider halfpipeBannerProvider() {
        return new HalfpipeBannerProvider();
    }

    @Bean @Scope("singleton")
    public OptionalQueryParamInjectableProvider optionalQueryParamInjectableProvider() {
        return new OptionalQueryParamInjectableProvider();
    }

    @Bean @Scope("singleton")
    public GuavaExtrasModule guavaExtrasModule() {
        return new GuavaExtrasModule();
    }

/*    @Bean @Scope("singleton")
    public DynamicPropertiesModule dynamicPropertiesModule() {
        return new DynamicPropertiesModule();
    }*/

    @Bean @Scope("singleton")
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    @Bean @Scope("singleton")
    public InvalidEntityExceptionMapper invalidEntityExceptionMapper() {
        return new InvalidEntityExceptionMapper();
    }

    @Bean @Scope("singleton")
    public ObjectMapper objectMapper(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                                     List<Module> modules) {
        return ObjectMapperFactory.create(namingStrategy, modules);
    }

    @Bean @Scope("singleton")
    public HalfpipeObjectMapperProvider objectMapperProvider(ObjectMapper objectMapper) {
        return new HalfpipeObjectMapperProvider(objectMapper);
    }

    @Bean @Scope("singleton")
    public AnnotationSensitivePropertyNamingStrategy jsonNamingStrategy() {
        return new AnnotationSensitivePropertyNamingStrategy();
    }

    @Bean @Scope("singleton")
    public JacksonMessageBodyProvider jacksonMessageBodyProvider(ObjectMapper objectMapper) {
        return new JacksonMessageBodyProvider(objectMapper);
    }

    @Bean @Scope("singleton")
    public DeadlockHealthCheck deadlockHealthCheck() {
        return new DeadlockHealthCheck();
    }

    @Bean @Scope("singleton")
    public HealthCheckRegistry healthChecks(List<HealthCheck> healthChecks) {
        for (HealthCheck healthCheck : healthChecks) {
            HealthChecks.register(healthCheck);
        }
        return HealthChecks.defaultRegistry();
    }

    @Bean @Scope("singleton")
    public HealthCheckRegistry healthCheckRegistry() {
        return HealthChecks.defaultRegistry();
    }

    @Bean @Scope("singleton")
    public MetricsRegistry metricsRegistry() {
        return Metrics.defaultRegistry();
    }

    @Bean @Scope("singleton") @Named("MetricsProxyConfig")
    public ProxyConfig proxyConfig() {
        return new ProxyConfig();
    }

}
