package halfpipe.client;

import halfpipe.util.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/30/14
 * Time: 10:35 PM
 */
@Configuration
public class ClientAutoConfig {

    @Inject
    ApplicationContext context;

    @Bean
    ClientProperties clientProperties() {
        return new ClientProperties();
    }

    @Bean
    BeanUtils halfpipeBeanUtils() {
        return new BeanUtils(context);
    }

}
