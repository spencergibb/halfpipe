package halfpipe.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);


        if (modules != null)
            for (Module module: modules)
                mapper.registerModule(module);

        return mapper;
    }
}
