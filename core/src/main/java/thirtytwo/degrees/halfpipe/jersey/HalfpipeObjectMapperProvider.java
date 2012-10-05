package thirtytwo.degrees.halfpipe.jersey;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
@Provider
public class HalfpipeObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper;

    public HalfpipeObjectMapperProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
