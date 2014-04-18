package halfpipe.swagger;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 4:21 PM
 */
@Configuration
@ConditionalOnClass(SwaggerConfig.class)
public class SwaggerAutoConfig {
    @Bean
    SwaggerController swaggerController() {
        return new SwaggerController();
    }

    @Bean
    SwaggerConfig swaggerConfig() {
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("1");
        config.setBasePath("http://localhost:8080/v1");
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
        return config;
    }

    @Bean
    ApiListingResourceJSON apiListingResourceJSON() {
        return new ApiListingResourceJSON();
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
