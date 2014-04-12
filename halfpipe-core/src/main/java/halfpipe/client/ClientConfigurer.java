package halfpipe.client;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class ClientConfigurer {

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder feignDecoder() {
        return new HystrixJacksonDecoder();
    }

    @Bean
    public Logger feignLogger() {
        return new Logger.NoOpLogger();
    }

    public class Builder<T> {
        private final Class<T> apiType;
        Feign.Builder feign = Feign.builder();

        private Builder(Class<T> apiType) {
            this.apiType = apiType;
        }

        public Builder<T> logLevel(Logger.Level logLevel) {
            feign.logLevel(logLevel);
            return this;
        }

        public Builder<T> contract(Contract contract) {
            feign.contract(contract);
            return this;
        }

        public Builder<T> client(Client client) {
            feign.client(client);
            return this;
        }

        public Builder<T> retryer(Retryer retryer) {
            feign.retryer(retryer);
            return this;
        }

        public Builder<T> logger(Logger logger) {
            feign.logger(logger);
            return this;
        }

        public Builder<T> encoder(Encoder encoder) {
            feign.encoder(encoder);
            return this;
        }

        public Builder<T> decoder(Decoder decoder) {
            feign.decoder(decoder);
            return this;
        }

        public Builder<T> errorDecoder(ErrorDecoder errorDecoder) {
            feign.errorDecoder(errorDecoder);
            return this;
        }

        public Builder<T> options(Request.Options options) {
            feign.options(options);
            return this;
        }

        public Builder<T> requestInterceptor(RequestInterceptor requestInterceptor) {
            feign.requestInterceptor(requestInterceptor);
            return this;
        }

        public Builder<T> requestInterceptors(Iterable<RequestInterceptor> requestInterceptors) {
            feign.requestInterceptors(requestInterceptors);
            return this;
        }

        @SuppressWarnings("unchecked")
        public T target(String url) {
            T target = feign.target(apiType, url);

            T proxyInstance = (T) Proxy.newProxyInstance(apiType.getClassLoader(),
                    new Class<?>[]{apiType},
                    new HystrixInvocationHandler<>(target));
            return proxyInstance;
        }

    }

    protected <T> Builder<T> builder(Class<T> apiType) {
        return new Builder<>(apiType)
                .logger(feignLogger())
                .encoder(feignEncoder())
                .decoder(feignDecoder());
    }

}
