package thirtytwo.degrees.halfpipe.jersey;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
@Provider
public class HalfpipeObjectMapperProvider implements ContextResolver<ObjectMapper> {

    GuavaModule guavaModule;
    GuavaExtrasModule guavaExtrasModule;

    public HalfpipeObjectMapperProvider(GuavaModule guavaModule, GuavaExtrasModule guavaExtrasModule) {
        this.guavaExtrasModule = guavaExtrasModule;
        this.guavaModule = guavaModule;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(guavaModule);
        mapper.registerModule(guavaExtrasModule);

        return mapper;
    }
}
