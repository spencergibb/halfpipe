package halfpipe.resteasy;

import halfpipe.core.ApplicationProperties;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.springmvc.ResteasyHandlerAdapter;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/15/14
 * Time: 7:56 PM
 */
@Configuration
@ConditionalOnClass(ResteasyDeployment.class)
@ImportResource("classpath:springmvc-resteasy.xml")
public class ResteasyAutoConfig extends WebMvcConfigurerAdapter {

    @Inject
    ResteasyHandlerMapping resteasyHandlerMapping;

    @Inject
    ResteasyHandlerAdapter resteasyHandlerAdapter;

    @Inject
    ResteasyDeployment resteasyDeployment;

    @Inject
    ApplicationProperties properties;

    @PostConstruct
    public void init() {
        resteasyHandlerMapping.setOrder(1);
        resteasyHandlerMapping.setPrefix(properties.getPrefix());
    }

    @Bean
    public HalfpipeInjectorFactory halfpipeInjectorFactory() {
        return new HalfpipeInjectorFactory();
    }

    @Bean(name = "resteasy.providerFactory")
    public ResteasyProviderFactory resteasyProviderFactory() {
        ResteasyProviderFactory providerFactory = resteasyDeployment.getProviderFactory();
        providerFactory.setInjectorFactory(halfpipeInjectorFactory());
        return providerFactory;
    }
/*
    @Bean(initMethod = "start", destroyMethod = "stop")
    public ResteasyDeployment resteasyDeployment() {
        return new ResteasyDeployment();
    }

    @Bean
    public Registry resteasyRegistry() {
        return resteasyDeployment().getRegistry();
    }

    @Bean
    public Dispatcher resteasyDispatcher() {
        return resteasyDeployment().getDispatcher();
    }


    @Bean
    public SpringBeanProcessor springBeanProcessor() {
        return new SpringBeanProcessor(resteasyDeployment());
    }*/
}
