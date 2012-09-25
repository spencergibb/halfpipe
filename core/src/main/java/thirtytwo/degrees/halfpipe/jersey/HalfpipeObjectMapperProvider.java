package thirtytwo.degrees.halfpipe.jersey;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.codehaus.jackson.map.*;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
@Provider
public class HalfpipeObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;

    public HalfpipeObjectMapperProvider(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                                        GuavaExtrasModule guavaExtrasModule,
                                        GuavaModule guavaModule) {
        this.objectMapper = ObjectMapperFactory.get(namingStrategy, guavaExtrasModule, guavaModule);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
