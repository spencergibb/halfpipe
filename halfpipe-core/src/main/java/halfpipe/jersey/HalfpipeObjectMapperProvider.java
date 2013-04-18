package halfpipe.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:35 PM
 */
@Provider
@Service
public class HalfpipeObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Inject
    ObjectMapper mapper;

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
