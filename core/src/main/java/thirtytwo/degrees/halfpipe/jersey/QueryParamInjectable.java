package thirtytwo.degrees.halfpipe.jersey;

import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.server.impl.model.parameter.multivalued.ExtractorContainerException;
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor;

// see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/jersey/QueryParamInjectable.java
public class QueryParamInjectable extends AbstractHttpContextInjectable<Object> {
    private final MultivaluedParameterExtractor extractor;
    private final boolean decode;

    public QueryParamInjectable(MultivaluedParameterExtractor extractor,
                                boolean decode) {
        this.extractor = extractor;
        this.decode = decode;
    }

    @Override
    public Object getValue(HttpContext c) {
        try {
            return extractor.extract(c.getUriInfo().getQueryParameters(decode));
        } catch (ExtractorContainerException e) {
            throw new ParamException.QueryParamException(e.getCause(),
                                                         extractor.getName(),
                                                         extractor.getDefaultStringValue());
        }
    }
}
