package halfpipe.config;

import halfpipe.client.ClientProperties;
import halfpipe.properties.ArchaiusPropertiesProcessor;
import halfpipe.properties.HalfpipeProperties;
import halfpipe.util.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

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

    @Bean
    ClientProperties clientProperties() {
        return new ClientProperties();
    }

    @Bean
    BeanUtils halfpipeBeanUtils(ApplicationContext context) {
        return new BeanUtils(context);
    }

    @Bean
    public ArchaiusPropertiesProcessor archaiusProcessor() {
        return new ArchaiusPropertiesProcessor();
    }

    @Bean
    public HalfpipeProperties halfpipeProperties() {
        return new HalfpipeProperties();
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

    /*@Bean
    public ConversionService conversionService(ApplicationContext applicationContext) {
        DefaultConversionService service = new DefaultConversionService();
        for (Converter<?, ?> converter : applicationContext.getBeansOfType(Converter.class).values()) {
            service.addConverter(converter);
        }
        Map<String, ConditionalGenericConverter> beansOfType = applicationContext.getBeansOfType(ConditionalGenericConverter.class);
        for (GenericConverter converter : beansOfType.values()) {
            service.addConverter(converter);
        }
        //service.addConverter(new StringToDynaPropConverter(service));
        return service;
    }*/
}
