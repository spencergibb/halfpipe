package halfpipe.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixExecutable;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import rx.Observable;

public class HystrixJacksonDecoder implements Decoder {
    private static final List<Class<?>> HYSTRIX_CLASSES = Lists.newArrayList(
            Future.class,
            HystrixExecutable.class,
            Observable.class
    );

    private final ObjectMapper mapper;

    public HystrixJacksonDecoder() {
        this(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    public HystrixJacksonDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if(response.body() == null) { //TODO: checking body length with resteasy led to NPE
            return null;
        }
        try {
            if (type instanceof ParameterizedType) {
                Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
                Type rawType = ((ParameterizedType) type).getRawType();
                if (HYSTRIX_CLASSES.contains(rawType)) {
                    //Let the Hystrix Proxy wrap this
                    return read(response, actualType);
                }
            } else if (HYSTRIX_CLASSES.contains(type)) {
                throw new DecodeException("Return type "+type+" must be parameterized");
            }

            return read(response, type);

        } catch(RuntimeJsonMappingException e) {
            if(e.getCause() != null && e.getCause() instanceof IOException) {
                throw IOException.class.cast(e.getCause());
            }
            throw e;
        }
    }

    private Object read(Response response, Type type) throws IOException {
        InputStream inputStream = response.body().asInputStream();
        return mapper.readValue(inputStream, mapper.constructType(type));
    }

}
