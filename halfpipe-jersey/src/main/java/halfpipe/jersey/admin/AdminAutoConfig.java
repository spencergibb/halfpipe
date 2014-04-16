package halfpipe.jersey.admin;

import com.netflix.adminresources.resources.*;
import com.netflix.adminresources.resources.jmx.JMXResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: spencergibb
 * Date: 4/12/14
 * Time: 8:50 PM
 */
@Configuration
public class AdminAutoConfig {

    @Bean
    JarsInfoResource jarsInfoResource() {
        return new JarsInfoResource();
    }

    @Bean
    EnvironmentResource environmentResource() {
        return new EnvironmentResource();
    }

    @Bean
    AdminPageResource adminPageResource() {
        return new AdminPageResource();
    }

    @Bean
    EmbeddedContentResource embeddedContentResource() {
        return new EmbeddedContentResource();
    }

    @Bean
    EurekaResource eurekaResource() {
        return new EurekaResource();
    }

    @Bean
    PropertiesResource propertiesResource() {
        return new PropertiesResource();
    }

    @Bean
    JMXResource jmxResource() {
        return new JMXResource();
    }
}
