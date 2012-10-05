package thirtytwo.degrees.halfpipe.jackson;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import thirtytwo.degrees.halfpipe.jackson.AnnotationSensitivePropertyNamingStrategy;

import java.util.List;

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
public class ObjectMapperFactory {

    public static ObjectMapper create(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                               List<Module> modules) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(namingStrategy);

        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING);


        if (modules != null)
            for (Module module: modules)
                mapper.registerModule(module);

        return mapper;
    }
}
