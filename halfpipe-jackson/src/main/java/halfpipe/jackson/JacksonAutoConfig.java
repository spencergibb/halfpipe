package halfpipe.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * User: spencergibb
 * Date: 5/1/14
 * Time: 9:19 AM
 */
@Configuration
public class JacksonAutoConfig {

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        //TODO auto config configurer ala boot
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new GuavaExtrasModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new JSR310Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public ObjectMapperProvider jerseyObjectMapperProvider() {
        return new ObjectMapperProvider(objectMapper);
    }
}
