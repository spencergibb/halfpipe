package halfpipe.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: spencergibb
 * Date: 4/30/14
 * Time: 10:41 PM
 */
@Configuration
public class PropertiesAutoConfig {

    @Bean
    public ArchaiusPropertiesProcessor archaiusPropertiesProcessor() {
        return new ArchaiusPropertiesProcessor();
    }

    @Bean
    public ArchaiusInitializer archaiusInitializer() {
        return new ArchaiusInitializer();
    }

}
