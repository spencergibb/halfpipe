package halfpipe.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class ClientConfigurer {

    @Inject
    ObjectMapper objectMapper;

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Decoder feignDecoder() {
        return new HystrixJacksonDecoder(objectMapper);
        //return new JacksonDecoder();
    }

    @Bean
    public Logger feignLogger() {
        return new Slf4jLogger(); //TODO pass Client classname in
    }

    @Bean
    public InvocationHandlerFactory invocationHandlerFactory() {
        return new HystrixInvocationHandler.Factory();
    }

    protected Feign.Builder client() {
        return Feign.builder()
                .logger(feignLogger())
                .encoder(feignEncoder())
                .decoder(feignDecoder())
                .invocationHandlerFactory(invocationHandlerFactory());
    }

}
