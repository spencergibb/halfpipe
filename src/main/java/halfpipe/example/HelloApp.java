package halfpipe.example;

import com.netflix.config.DynamicPropertyFactory;
import halfpipe.properties.ArchaiusPropertiesProcessor;
import halfpipe.example.config.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Map;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = HelloApp.class)
public class HelloApp {

    @Bean
    public ArchaiusPropertiesProcessor archaiusProcessor() {
        return new ArchaiusPropertiesProcessor();
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/v1/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }

    //TODO: move to core?
    @Bean
    public ConversionService conversionService(BeanFactory beanFactory) {
        DefaultConversionService service = new DefaultConversionService();
        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
        for (Converter<?, ?> converter : listableBeanFactory.getBeansOfType(Converter.class).values()) {
            service.addConverter(converter);
        }
        Map<String, ConditionalGenericConverter> beansOfType = listableBeanFactory.getBeansOfType(ConditionalGenericConverter.class);
        for (GenericConverter converter : beansOfType.values()) {
            service.addConverter(converter);
        }
        //service.addConverter(new StringToDynaPropConverter(service));
        return service;
    }

    public static void main(String[] args) {
        System.setProperty("archaius.configurationSource.defaultFileName", "application.properties");
        DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        SpringApplication.run(HelloApp.class, args);
    }
}