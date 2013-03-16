package thirtytwo.degrees.halfpipe.example.core;

import com.google.common.base.Optional;
import com.netflix.config.DynamicStringProperty;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 5:45 PM
 */
public class Hello {
    @NotEmpty
    String hello;

    @NotEmpty
    String to;

    Child child;

    public Hello(@JsonProperty("hello") String hello, @JsonProperty("to") String to,
                 @JsonProperty("child") Child child) {
        this.hello = hello;
        this.to = to;
        this.child = child;
    }

    public String getHello() {
        return hello;
    }

    public String getTo() {
        return to;
    }

    public Child getChild() {
        return child;
    }
}
