package halfpipe.consul;

import com.google.common.base.Supplier;
import halfpipe.client.ClientConfigurer;
import halfpipe.consul.client.AgentClient;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.KeyValue;
import halfpipe.properties.PropertiesSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:16 PM
 */
@Configuration
public class ConsulAutoConfig extends ClientConfigurer {

    @Bean
    ConsulProperties consulProperties() {
        return new ConsulProperties();
    }

    @Bean
    PropertiesSourceFactory consulPropertiesSourceFactory() {
        return new ConsulPropertiesSourceFactory();
    }

    @Bean
    ConsulPropertiesSource consulPropertiesSource() {
        return new ConsulPropertiesSource();
    }

    @Bean
    ShutdownDeregister shutdownDeregister() {
        return new ShutdownDeregister();
    }

    @Bean(name = "getKeyValueRecurse.fallback")
    Supplier<List<KeyValue>> getKeyValueRecurseFallback() {
        return new Supplier<List<KeyValue>>() {
            @Override
            public List<KeyValue> get() {
                return new ArrayList<>();
            }
        };
    }

    @Bean
    KVClient kvClient() {
        return builder().target(KVClient.class, consulProperties().getHost());
    }

    @Bean
    AgentClient agentClient() {
        return builder().target(AgentClient.class, consulProperties().getHost());
    }
}
