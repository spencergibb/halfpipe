package halfpipe.context;

import halfpipe.configuration.builder.PropBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import halfpipe.configuration.ConfigurationBuilder;

import java.util.List;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
@Import(BaseContext.class)
public class DefaultContext {

    //TODO: use spring profiles for java vs scala stuff
    @Bean @Scope("singleton")
    public ConfigurationBuilder configurationBuilder(ConversionService conversionService, List<PropBuilder<?, ?>> builders) {
        return new ConfigurationBuilder(conversionService, builders);
    }
}
