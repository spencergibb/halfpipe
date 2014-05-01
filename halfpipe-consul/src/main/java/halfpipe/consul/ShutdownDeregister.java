package halfpipe.consul;

import com.netflix.hystrix.Hystrix;
import halfpipe.consul.client.AgentClient;
import halfpipe.core.ApplicationProperties;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 4:48 PM
 */
public class ShutdownDeregister {
    @Inject
    AgentClient agentClient;

    @Inject
    ConsulProperties consulProperties;

    @Inject
    ApplicationProperties properties;

    @PreDestroy
    public void destroy() {
        if (consulProperties.isEnabled()) {
            agentClient.deregister(properties.getId()); //TODO: non-hystrix client?
        }
        Hystrix.reset(); //TODO: where should this live.  It needs be after the above.
    }
}
