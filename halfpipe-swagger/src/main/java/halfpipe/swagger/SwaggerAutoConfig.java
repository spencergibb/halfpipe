package halfpipe.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 4:21 PM
 */
@Configuration
@ConditionalOnClass(SwaggerConfig.class)
public class SwaggerAutoConfig {
    @Inject
    ObjectMapper objectMapper;

    @Bean
    SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    SwaggerEndpointProps swaggerEndpointProps() {
        return new SwaggerEndpointProps();
    }

    @Bean
    SwaggerEndpoint swaggerEndpoint() {
        return new SwaggerEndpoint(swaggerEndpointProps());
    }

    @Bean
    SwaggerConfig swaggerConfig() {
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion(swaggerProperties().getApiVersion().get());
        config.setBasePath(swaggerProperties().getBasePath().get());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        //TODO: move to another module?
        objectMapper.registerModule(new DefaultScalaModule());

        return config;
    }

    @Bean
    ApiDeclarationProvider apiDeclarationProvider() {
        return new ApiDeclarationProvider();
    }

    @Bean
    ResourceListingProvider resourceListingProvider() {
        return new ResourceListingProvider();
    }
}
