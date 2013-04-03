package halfpipe.context;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.yammer.metrics.HealthChecks;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.HealthCheck;
import com.yammer.metrics.core.HealthCheckRegistry;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.util.DeadlockHealthCheck;
import halfpipe.cli.HalfpipeBannerProvider;
import halfpipe.cli.HalfpipeServer;
import halfpipe.configuration.ConfigurationBeanPostProcessor;
import halfpipe.configuration.convert.StringToTimeZoneConverter;
import halfpipe.jackson.AnnotationSensitivePropertyNamingStrategy;
import halfpipe.jackson.GuavaExtrasModule;
import halfpipe.jackson.ObjectMapperFactory;
import halfpipe.jersey.HalfpipeObjectMapperProvider;
import halfpipe.jersey.InvalidEntityExceptionMapper;
import halfpipe.jersey.JacksonMessageBodyProvider;
import halfpipe.jersey.OptionalQueryParamInjectableProvider;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
public class BaseContext {

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

    @Bean @Scope("singleton")
    public ConfigurationBeanPostProcessor configurationBeanPostProcessor() {
        return new ConfigurationBeanPostProcessor();
    }

    @Bean @Scope("singleton")
    public HalfpipeServer server() {
        return new HalfpipeServer();
    }

    @Bean @Scope("singleton")
    public StringToTimeZoneConverter stringToTimeZoneConverter() {
        return new StringToTimeZoneConverter();
    }

    @Bean @Scope("singleton")
    public ConversionServiceFactoryBean conversionServiceFactoryBean(Set<Converter> converters) {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(converters);
        return factoryBean;
    }

}
