package thirtytwo.degrees.halfpipe.jersey;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.codehaus.jackson.map.*;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 5:10 PM
 */
public class ObjectMapperFactory {
    public static ObjectMapper get(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                            GuavaExtrasModule guavaExtrasModule,
                            GuavaModule guavaModule) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(namingStrategy);

        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING);

        mapper.registerModule(guavaExtrasModule);
        mapper.registerModule(guavaModule);
        return mapper;

    }
}
