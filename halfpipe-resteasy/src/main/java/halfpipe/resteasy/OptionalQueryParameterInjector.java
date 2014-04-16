package halfpipe.resteasy;

import com.google.common.base.Optional;
import org.jboss.resteasy.core.StringParameterInjector;
import org.jboss.resteasy.core.ValueInjector;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.metadata.Parameter;

import javax.ws.rs.QueryParam;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
* User: spencergibb
* Date: 4/16/14
* Time: 4:41 PM
*/
public class OptionalQueryParameterInjector extends StringParameterInjector implements ValueInjector {
    private final boolean encode;
    private final String encodedName;

    public OptionalQueryParameterInjector(Class<?> targetType, Parameter parameter, ResteasyProviderFactory factory) {
        super(targetType, parameter.getGenericType(), parameter.getParamName(), QueryParam.class,
                parameter.getDefaultValue(), parameter.getAccessibleObject(), parameter.getAnnotations(), factory);
        this.encode = parameter.isEncoded();
        try {
            this.encodedName = URLDecoder.decode(paramName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BadRequestException("Unable to decode query string", e);
        }
    }

    @Override
    public Object inject() {
        throw new RuntimeException("It is illegal to inject a @QueryParam into a singleton");
    }

    @Override
    public Object inject(HttpRequest request, HttpResponse response) {
        String pname = (encode) ? encodedName : paramName;
        boolean decode = !encode;
        List<String> list = request.getUri().getQueryParameters(decode).get(pname);
        Object o = extractValues(list);
        return Optional.fromNullable(o);
    }
}
