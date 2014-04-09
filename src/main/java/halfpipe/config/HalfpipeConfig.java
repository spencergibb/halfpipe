package halfpipe.config;

import halfpipe.jersey.HalfpipeResourceConfig;
import halfpipe.properties.ArchaiusPropertiesProcessor;
import halfpipe.properties.HalfpipeProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:07 AM
 */
@Configuration

public class HalfpipeConfig {

    @Bean
    public ArchaiusPropertiesProcessor archaiusProcessor() {
        //TODO: configurable?
        System.setProperty("archaius.configurationSource.defaultFileName", "application.properties");
        return new ArchaiusPropertiesProcessor();
    }

    @Bean
    public HalfpipeResourceConfig jerseyConfig() {
        return new HalfpipeResourceConfig();
    }

    @Bean
    public HalfpipeProperties halfpipeProperties() {
        return new HalfpipeProperties();
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        return new ServletRegistrationBean(new ServletContainer(jerseyConfig()), halfpipeProperties().getUrlMapping());
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
}
