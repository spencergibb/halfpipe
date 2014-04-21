package halfpipe.consul;

import halfpipe.consul.client.AgentClient;
import halfpipe.consul.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 3:57 PM
 */
public class ConsulContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulContextRefreshedListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        ConsulProperties consulProperties = context.getBean(ConsulProperties.class);
        if (!consulProperties.isEnabled())
            return;

        ServerProperties serverProperties = context.getBean(ServerProperties.class);
        AgentClient agentClient = context.getBean(AgentClient.class);

        String serviceName = consulProperties.getServiceName();
        if (serviceName == null) {
            LOGGER.warn("Consul is enabled, but serviceName is null, not registering");
            return;
        }

        Service service = new Service();
        service.setName(serviceName);
        Integer port = serverProperties.getPort();
        if (port == null) {
            port = 8080;
        }
        service.setPort(port);
        service.setTags(consulProperties.getTags());

        //TODO: add support for Check

        agentClient.register(service);
    }
}
