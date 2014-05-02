package halfpipe.web;

import lombok.Data;
import org.springframework.boot.actuate.endpoint.Endpoint;

/**
 * User: spencergibb
 * Date: 4/22/14
 * Time: 8:59 AM
 */
@Data
public class EndpointDelegate implements Endpoint<Object> {
    private final String id;
    private final boolean enabled;
    private final boolean sensitive;

    public EndpointDelegate(EndpointDynaProps props) {
        id = props.getId().get();
        enabled = props.getEnabled().get();
        sensitive = props.getSensitive().get();
    }

    public EndpointDelegate(EndpointProperties props) {
        assert props.getId() != null;
        id = props.getId();
        enabled = props.isEnabled();
        sensitive = props.isSensitive();
    }

    @Override
    public Object invoke() {
        throw new UnsupportedOperationException("EndpointDelegate.invoke should never be called");
    }
}
