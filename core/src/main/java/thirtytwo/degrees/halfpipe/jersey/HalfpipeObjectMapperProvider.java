package thirtytwo.degrees.halfpipe.jersey;

import static com.google.common.collect.Iterables.*;
import org.codehaus.jackson.map.*;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.List;

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
@Provider
public class HalfpipeObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper;

    public HalfpipeObjectMapperProvider(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                                        List<Module> modules) {
        mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(namingStrategy);

        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING);


        if (modules != null)
            for (Module module: modules)
                mapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
