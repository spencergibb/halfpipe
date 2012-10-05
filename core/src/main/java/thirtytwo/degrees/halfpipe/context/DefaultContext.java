package thirtytwo.degrees.halfpipe.context;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
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
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import thirtytwo.degrees.halfpipe.cli.HalfpipeBannerProvider;
import thirtytwo.degrees.halfpipe.configuration.ConfigurationBeanPostProcessor;
import thirtytwo.degrees.halfpipe.configuration.ConfigurationBuilder;
import thirtytwo.degrees.halfpipe.jackson.AnnotationSensitivePropertyNamingStrategy;
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
@Import(BaseContext.class)
public class DefaultContext {

    @Bean @Scope("singleton")
    public ConfigurationBuilder configurationBuilder() {
        return new ConfigurationBuilder();
    }
}
