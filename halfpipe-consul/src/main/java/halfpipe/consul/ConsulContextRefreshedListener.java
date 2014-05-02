package halfpipe.consul;

import halfpipe.consul.client.AgentClient;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.Service;
import halfpipe.core.ApplicationProperties;
import halfpipe.util.RunOnceApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 3:57 PM
 */
public class ConsulContextRefreshedListener extends RunOnceApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulContextRefreshedListener.class);

    @Override
    public void onApplicationEventInternal(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        ConsulProperties consulProperties = context.getBean(ConsulProperties.class);
        if (!consulProperties.isEnabled())
            return;

        ApplicationProperties appProps = context.getBean(ApplicationProperties.class);
        ServerProperties serverProperties = context.getBean(ServerProperties.class);
        AgentClient agentClient = context.getBean(AgentClient.class);

        Service service = new Service();
        service.setName(appProps.getId());
        Integer port = serverProperties.getPort();
        if (port == null) {
            port = 8080;
        }
        service.setPort(port);
        service.setTags(consulProperties.getTags());

        //TODO: add support for Check

        register(agentClient, service);

        String managementPort = context.getEnvironment().getProperty("management.port", (String) null);
        if (managementPort != null) {
            Service management = new Service();
            management.setName(appProps.getId() + "/management"); //TODO: configurable management suffix
            management.setPort(Integer.parseInt(managementPort));
            List<String> tags = new ArrayList<>(consulProperties.getTags());
            tags.add("management"); //TODO: configurable management tag
            management.setTags(tags);

            register(agentClient, management);
        }

        if (!appProps.getRoutes().isEmpty()) {
            try {
                KVClient kvClient = context.getBean(KVClient.class);

                String key = String.format("routing/%s", appProps.getId());
                //TODO: get routes from jax-rs and spring mvc
                kvClient.put(key, appProps.getRoutes());
            } catch (Exception e) {
                LOGGER.error("Error writing routes for app: " + appProps.getId(), e);
            }
        }
    }

    private void register(AgentClient agentClient, Service service) {
        LOGGER.info("Registering service with consul: {}", service.toString());
        agentClient.register(service);
    }
}
