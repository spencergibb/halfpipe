package halfpipe.consul;

import com.netflix.hystrix.Hystrix;
import halfpipe.consul.ConsulProperties;
import halfpipe.consul.client.AgentClient;

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

    @PreDestroy
    public void destroy() {
        agentClient.deregister(consulProperties.getServiceName()); //TODO: non-hystrix client?
        Hystrix.reset(); //TODO: where should this live.  It needs be after the above.
    }
}
