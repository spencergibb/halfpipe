package thirtytwo.degrees.halfpipe.example.core;

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

    public Hello(@JsonProperty("hello") String hello, @JsonProperty("to") String to) {
        this.hello = hello;
        this.to = to;
    }

    public String getHello() {
        return hello;
    }

    public String getTo() {
        return to;
    }
}
