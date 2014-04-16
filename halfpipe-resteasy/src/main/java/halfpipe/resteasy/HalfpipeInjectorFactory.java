package halfpipe.resteasy;

import com.google.common.base.Optional;
import org.jboss.resteasy.core.InjectorFactoryImpl;
import org.jboss.resteasy.core.ValueInjector;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.metadata.Parameter;

import javax.ws.rs.ext.Provider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
        return super.createParameterExtractor(parameter, providerFactory);
    }

}
