package halfpipe.resteasy;

import com.google.common.base.Optional;
import org.jboss.resteasy.core.InjectorFactoryImpl;
import org.jboss.resteasy.core.QueryParamInjector;
import org.jboss.resteasy.core.StringParameterInjector;
import org.jboss.resteasy.core.ValueInjector;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.metadata.Parameter;
import org.jboss.resteasy.util.Types;

import javax.ws.rs.QueryParam;
import javax.ws.rs.ext.Provider;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/16/14
 * Time: 1:24 PM
 */
@Provider
public class HalfpipeInjectorFactory extends InjectorFactoryImpl {
    @Override
    public ValueInjector createParameterExtractor(final Parameter parameter, ResteasyProviderFactory providerFactory) {
        if (parameter.getParamType() == Parameter.ParamType.QUERY_PARAM
                && Optional.class.isAssignableFrom(parameter.getType())) {
            ParameterizedType genericType = (ParameterizedType) parameter.getGenericType();
            Type type = genericType.getActualTypeArguments()[0];
            return new OptionalQueryParameterInjector((Class<?>)type, parameter, providerFactory);
        }
        return super.createParameterExtractor(parameter, providerFactory);    //TODO: implement HalfpipeInjectorFactory.createParameterExtractor
    }

    private static class OptionalQueryParameterInjector extends StringParameterInjector implements ValueInjector {
        private final boolean encode;
        private final String encodedName;

        private OptionalQueryParameterInjector(Class<?> targetType, Parameter parameter, ResteasyProviderFactory factory) {
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
}
