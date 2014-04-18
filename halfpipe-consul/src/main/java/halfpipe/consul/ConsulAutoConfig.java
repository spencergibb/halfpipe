package halfpipe.consul;

import halfpipe.client.ClientConfigurer;
import halfpipe.consul.keyvalue.ConsulPropertiesSource;
import halfpipe.consul.keyvalue.KVClient;
import halfpipe.properties.PropertiesSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:16 PM
 */
@Configuration
public class ConsulAutoConfig extends ClientConfigurer {

    @Bean
    PropertiesSourceFactory consulPropertiesSourceFactory() {
        return new ConsulPropertiesSource.Factory();
    }

    @Bean
    ConsulPropertiesSource consulPropertiesSource() {
        return new ConsulPropertiesSource();
    }

    @Bean
    KVClient kvClient() {
        //TODO: config consul host
        return builder().target(KVClient.class, "http://localhost:8500");
    }
}
