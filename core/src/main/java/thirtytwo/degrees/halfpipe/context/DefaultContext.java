package thirtytwo.degrees.halfpipe.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import thirtytwo.degrees.halfpipe.configuration.ConfigurationBuilder;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
@Import(BaseContext.class)
public class DefaultContext {

    @Bean @Scope("singleton")
    public ConfigurationBuilder configurationBuilder(ConversionService conversionService) {
        return new ConfigurationBuilder(conversionService);
    }
}
