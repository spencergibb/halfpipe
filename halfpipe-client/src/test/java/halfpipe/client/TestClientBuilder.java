package halfpipe.client;

import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: spencergibb
 * Date: 5/27/14
 * Time: 1:47 PM
 */
public class TestClientBuilder {

    @Autowired
    Encoder encoder;

    @Autowired
    Decoder decoder;

    @Autowired
    Logger logger;

    @Autowired
    InvocationHandlerFactory invocationHandlerFactory;

    public Feign.Builder client() {
        return Feign.builder()
                .logger(logger)
                .encoder(encoder)
                .decoder(decoder)
                .invocationHandlerFactory(invocationHandlerFactory)
                .logLevel(Logger.Level.FULL);
    }
}
