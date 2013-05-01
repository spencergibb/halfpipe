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
import halfpipe.cli.HalfpipeServer;
import halfpipe.configuration.ConfigurationBeanPostProcessor;
import halfpipe.configuration.builder.PropBuilder;
import halfpipe.configuration.convert.StringToTimeZoneConverter;
import halfpipe.jackson.AnnotationSensitivePropertyNamingStrategy;
import halfpipe.jackson.ObjectMapperFactory;
import halfpipe.jersey.JerseyLogger;
import halfpipe.jetty.ServerFactory;
import halfpipe.logging.LoggingFactory;
import halfpipe.validation.ConstraintMappingResource;
import halfpipe.validation.HalfpipeValidator;
import halfpipe.web.ServletEnvironment;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.inject.Named;
import javax.validation.ConstraintValidator;
import java.util.List;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
@ComponentScan(basePackageClasses = {
        PropBuilder.class,
        ServletEnvironment.class,
        HalfpipeServer.class,
        LoggingFactory.class,
        HalfpipeValidator.class,
        JerseyLogger.class,
        AnnotationSensitivePropertyNamingStrategy.class,
        StringToTimeZoneConverter.class,
        ConfigurationBeanPostProcessor.class,
        ServerFactory.class
})
public class BaseContext {

    @Bean @Scope("singleton")
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    @Bean @Scope("singleton")
    public ObjectMapper objectMapper(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                                     List<Module> modules) {
        return ObjectMapperFactory.create(namingStrategy, modules);
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
    public LocalValidatorFactoryBean validator(Set<ConstraintValidator> validators) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setMappingLocations(new Resource[]{new ConstraintMappingResource(validators)});
        return bean;
    }

    @Bean @Scope("singleton")
    public ConversionServiceFactoryBean conversionServiceFactoryBean(Set<Converter> converters) {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(converters);
        return factoryBean;
    }

}
