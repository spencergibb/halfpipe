package halfpipe.consul;

import com.fasterxml.jackson.databind.ObjectMapper;
import halfpipe.core.ApplicationProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:42 PM
 */
@Configuration
public class TestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "mvcConversionService")
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        return service;
    }

    @Bean
    public ServletContext servletContext() {
        return new MockServletContext();
    }

    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    @Bean
    public ApplicationProperties halfpipeProperties(ApplicationContext context) {
        ApplicationProperties appProps = new ApplicationProperties();
        appProps.setId("testId");
        return appProps;
    }
}
