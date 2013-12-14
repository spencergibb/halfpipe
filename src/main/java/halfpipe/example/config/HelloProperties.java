package halfpipe.example.config;

import static halfpipe.config.DynaProp.*;

import halfpipe.config.DynaProp;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 4:43 PM
 */
@Component
@ConfigurationProperties("hello")
public class HelloProperties {

    private DynaProp<String> defaultMessage = prop("HelloWorld");

    public HelloProperties() {
        System.out.println("here");
    }

    public String getDefaultMessage() {
        return defaultMessage.get();
    }

    public void setDefaultMessage(DynaProp<String> defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
