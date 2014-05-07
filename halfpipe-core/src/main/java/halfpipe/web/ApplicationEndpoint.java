package halfpipe.web;

import halfpipe.core.ApplicationProperties;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 5/7/14
 * Time: 10:09 AM
 */
public class ApplicationEndpoint extends EndpointMvcAdapter {

    @Inject
    ApplicationProperties applicationProps;

    @Inject
    public ApplicationEndpoint(ApplicationEndpointProps props) {
        super(new EndpointDelegate(props));
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public Object invoke() {
        return applicationProps;
    }
}
