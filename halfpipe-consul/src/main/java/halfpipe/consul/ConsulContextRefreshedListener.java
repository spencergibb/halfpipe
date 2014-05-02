package halfpipe.consul;

import com.fasterxml.jackson.databind.ObjectMapper;
import halfpipe.consul.client.AgentClient;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.Service;
import halfpipe.core.ApplicationProperties;
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

        String managementPort = context.getEnvironment().getProperty("management.port", (String) null);
        //TODO: add support for management port: tags? convention

        //TODO: add support for Check

        LOGGER.info("Registering service {} with consul", appProps.getId());

        agentClient.register(service);

        if (!appProps.getRoutes().isEmpty()) {
            try {
                KVClient kvClient = context.getBean(KVClient.class);
                ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

                String key = String.format("routing/%s", appProps.getId());
                //TODO: get routes from jax-rs and spring mvc
                kvClient.put(key, appProps.getRoutes());
            } catch (Exception e) {
                LOGGER.error("Error writing routes for app: "+appProps.getId(), e);
            }
        }
    }
}
