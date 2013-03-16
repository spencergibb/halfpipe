package thirtytwo.degrees.halfpipe.context;

import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.spring.ExceptionMeteredAnnotationBeanPostProcessor;
import com.yammer.metrics.spring.GaugeAnnotationBeanPostProcessor;
import com.yammer.metrics.spring.MeteredAnnotationBeanPostProcessor;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;

/**
 * User: spencergibb
 * Date: 10/3/12
 * Time: 3:42 PM
 */
@Configuration
public class MetricsContext {

    @Bean
    @Scope("singleton")
    public ExceptionMeteredAnnotationBeanPostProcessor exceptionMeteredAnnotationBeanPostProcessor(
            MetricsRegistry metricsRegistry, @Named("MetricsProxyConfig") ProxyConfig proxyConfig)
    {
        return new ExceptionMeteredAnnotationBeanPostProcessor(metricsRegistry, proxyConfig, null);
    }

    @Bean @Scope("singleton")
    public MeteredAnnotationBeanPostProcessor meteredAnnotationBeanPostProcessor(
            MetricsRegistry metricsRegistry, @Named("MetricsProxyConfig") ProxyConfig proxyConfig)
    {
        return new MeteredAnnotationBeanPostProcessor(metricsRegistry, proxyConfig, null);
    }

    @Bean @Scope("singleton")
    public TimedAnnotationBeanPostProcessor timedAnnotationBeanPostProcessor(
            MetricsRegistry metricsRegistry, @Named("MetricsProxyConfig") ProxyConfig proxyConfig)
    {
        return new TimedAnnotationBeanPostProcessor(metricsRegistry, proxyConfig, null);
    }

    @Bean @Scope("singleton")
    public GaugeAnnotationBeanPostProcessor gaugeAnnotationBeanPostProcessor(
            MetricsRegistry metricsRegistry)
    {
        return new GaugeAnnotationBeanPostProcessor(metricsRegistry, null);
    }
}
