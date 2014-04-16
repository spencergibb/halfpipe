package halfpipe.jersey;

import com.google.common.base.Optional;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.internal.inject.*;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Value factory provider supporting the {@link QueryParam &#64;QueryParam} injection annotation.
 *
 * @author Paul Sandoz
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
@Singleton
@Provider
public class OptionalQueryParamValueFactoryProvider extends AbstractValueFactoryProvider {

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(OptionalQueryParamValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(OptionalInjectionResolver.class).to(new TypeLiteral<InjectionResolver<QueryParam>>() {
            }).in(Singleton.class);
            //bind(OptionalParamConverterProvider.class).in(Singleton.class);
        }
    }

    /**
     * {@link QueryParam &#64;QueryParam} injection resolver.
     */
    @Singleton
    public static final class OptionalInjectionResolver extends ParamInjectionResolver<QueryParam> {

        /**
         * Create new {@link QueryParam &#64;QueryParam} injection resolver.
         */
        public OptionalInjectionResolver() {
            super(OptionalQueryParamValueFactoryProvider.class);
        }
    }

    private static final class QueryParamValueFactory extends AbstractContainerRequestValueFactory<Object> {

        private final MultivaluedParameterExtractor<?> extractor;
        private final boolean decode;

        QueryParamValueFactory(MultivaluedParameterExtractor<?> extractor, boolean decode) {
            this.extractor = extractor;
            this.decode = decode;
        }

        @Override
        public Object provide() {
            try {
                return extractor.extract(getContainerRequest().getUriInfo().getQueryParameters(decode));
            } catch (ExtractorException e) {
                throw new ParamException.QueryParamException(e.getCause(),
                        extractor.getName(), extractor.getDefaultValueString());
            }
        }
    }
    @Provider
    public static class OptionalParamConverterProvider implements ParamConverterProvider {

        private final ServiceLocator serviceLocator;

        public OptionalParamConverterProvider(@Context ServiceLocator serviceLocator) {
            this.serviceLocator = serviceLocator;
            System.out.println("OptionalParamConverterProvider");
        }

        @Override
        public <T> ParamConverter<T> getConverter(final Class<T> rawType, Type genericType, final Annotation[] annotations) {
            if (rawType != Optional.class)
                return null;

            final ParamConverterFactory converterFactory = serviceLocator.getService(ParamConverterFactory.class);
            final Type typeParameter = ((ParameterizedType) genericType).getActualTypeArguments()[0];

            return new ParamConverter<T>() {

                @Override
                public T fromString(final String value) {
                    ParamConverter<Object> converter = converterFactory.getConverter((Class<Object>) typeParameter, typeParameter, annotations);
                    return rawType.cast(Optional.fromNullable(converter.fromString(value)));
                }

                @Override
                public String toString(final T value) throws IllegalArgumentException {
                    return value.toString();
                }
            };
        }
    }


    private static class OptionalExtractor implements MultivaluedParameterExtractor {
        private final MultivaluedParameterExtractor extractor;

        private OptionalExtractor(MultivaluedParameterExtractor extractor) {
            this.extractor = extractor;
        }

        @Override
        public String getName() {
            return extractor.getName();
        }

        @Override
        public String getDefaultValueString() {
            return extractor.getDefaultValueString();
        }

        @Override
        public Object extract(MultivaluedMap parameters) {
            return Optional.fromNullable(extractor.extract(parameters));
        }
    }

    /**
     * Injection constructor.
     *
     * @param mpep    multivalued map parameter extractor provider.
     * @param locator HK2 service locator.
     */
    @Inject
    public OptionalQueryParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator) {
        super(mpep, locator, Parameter.Source.QUERY);
    }

    @Override
    public AbstractContainerRequestValueFactory<?> createValueFactory(Parameter parameter) {

        if (isExtractable(parameter)) {
            MultivaluedParameterExtractor e = new OptionalExtractor(get(unpack(parameter)));
            if (e == null) {
                return new QueryParamValueFactory(e, !parameter.isEncoded());
            }
        }

        return null;
    }

    private boolean isExtractable(Parameter param) {
        return (param.getSourceName() != null) && !param.getSourceName().isEmpty() &&
                param.getRawType().isAssignableFrom(Optional.class) &&
                (param.getType() instanceof ParameterizedType);
    }

    private Parameter unpack(Parameter param) {
        final Type typeParameter = ((ParameterizedType) param.getType()).getActualTypeArguments()[0];
        return Parameter.create(param.getRawType(),
                (Class<?>) param.getType(),
                param.isEncoded(),
                (Class<?>) typeParameter,
                typeParameter,
                param.getAnnotations());
    }

}
