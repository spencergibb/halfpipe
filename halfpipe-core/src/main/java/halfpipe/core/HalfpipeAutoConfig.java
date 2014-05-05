package halfpipe.core;

import halfpipe.util.BeanUtils;
import halfpipe.web.HystrixStreamEndpoint;
import halfpipe.web.WarController;
import halfpipe.web.WarHandlerMapping;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:07 AM
 * TODO: spring @ConditionalOn*
 * @ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class })
 * @ConditionalOnExpression("${spring.aop.auto:true}")
 */
@Configuration
public class HalfpipeAutoConfig {
    @Inject
    ApplicationProperties properties;

    @Inject
    BeanUtils beanUtils;

    @Bean
    public ApplicationProperties halfpipeProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public HystrixStreamEndpoint hystrixStreamEndpoint() {
        return new HystrixStreamEndpoint();
    }

    /**
     * TODO: WebMvcConfigurationSupport.defaultServletHandlerMapping runs with a null servletContext
     * @param servletContext
     * @return
     */
    @Bean
    public HandlerMapping defaultServletHandlerMapping(ServletContext servletContext) {
        LocalConfigurer configurer = new LocalConfigurer(servletContext);
        AbstractHandlerMapping handlerMapping = configurer.getHandlerMapping();
        handlerMapping = handlerMapping != null ? handlerMapping : new AbstractHandlerMapping(){
            @Override
            protected Object getHandlerInternal(HttpServletRequest request) throws Exception { return null; }
        };
        return handlerMapping;
    }

    @Bean
    @ConditionalOnExpression("'${application.embeddedWar}' != null")
    WebAppContext webAppContext () {
        WebAppContext webapp = new WebAppContext();
        //webapp.setContextPath(properties.getEmbeddedWar().getPath());
        webapp.setContextPath("/");
        webapp.setWar(properties.getEmbeddedWar().getLocation());
        webapp.setExtractWAR(false);
        return webapp;
    }

    @Bean
    @ConditionalOnBean(WebAppContext.class)
    WarController warController() throws Exception {
        return new WarController(properties.getEmbeddedWar(), webAppContext());
    }

    @Bean
    @ConditionalOnBean(WebAppContext.class)
    public EmbeddedServletContainerCustomizer containerCustomizer(final WebAppContext webapp) {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer containerFactory) {
                if (containerFactory instanceof JettyEmbeddedServletContainerFactory) {
                    JettyEmbeddedServletContainerFactory factory = (JettyEmbeddedServletContainerFactory) containerFactory;
                    factory.addServerCustomizers(new JettyServerCustomizer() {
                        @Override
                        public void customize(Server server) {
                            webapp.setServer(server);
                        }
                    });
                }
            }
        };
    }

    @Bean
    @ConditionalOnBean(WebAppContext.class)
    WarHandlerMapping warHandlerMapping(Collection<? extends WarController> controllers) {
        return new WarHandlerMapping(controllers);
    }


    private class LocalConfigurer extends DefaultServletHandlerConfigurer {
        public LocalConfigurer(ServletContext servletContext) {
            super(servletContext);
        }

        @Override
        public AbstractHandlerMapping getHandlerMapping() {
            return super.getHandlerMapping();
        }
    }

}
