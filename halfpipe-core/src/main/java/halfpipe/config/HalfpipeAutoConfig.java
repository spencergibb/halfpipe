package halfpipe.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import halfpipe.client.ClientProperties;
import halfpipe.jackson.GuavaExtrasModule;
import halfpipe.jackson.ObjectMapperProvider;
import halfpipe.web.HystrixStreamEndpoint;
import halfpipe.properties.ArchaiusInitializer;
import halfpipe.properties.ArchaiusPropertiesProcessor;
import halfpipe.properties.HalfpipeProperties;
import halfpipe.util.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
    BeanUtils beanUtils;

    @Inject
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        //TODO auto config configurer ala boot
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new GuavaExtrasModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new JSR310Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public ObjectMapperProvider jerseyObjectMapperProvider() {
        return new ObjectMapperProvider(objectMapper);
    }

    @Bean
    public HalfpipeProperties halfpipeProperties() {
        return new HalfpipeProperties();
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
