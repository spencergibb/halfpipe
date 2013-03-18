package halfpipe.jersey;

import com.google.common.base.Optional;
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor;

import javax.ws.rs.core.MultivaluedMap;

// see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/jersey/OptionalExtractor.java
public class OptionalExtractor implements MultivaluedParameterExtractor {
    private final MultivaluedParameterExtractor extractor;

    public OptionalExtractor(MultivaluedParameterExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public String getName() {
        return extractor.getName();
    }

    @Override
    public String getDefaultStringValue() {
        return extractor.getDefaultStringValue();
    }

    @Override
    public Object extract(MultivaluedMap<String, String> parameters) {
        return Optional.fromNullable(extractor.extract(parameters));
    }
}
