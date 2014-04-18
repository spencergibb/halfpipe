package halfpipe.consul;

import halfpipe.consul.client.AgentClient;
import halfpipe.consul.model.Service;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        ServerProperties serverProperties = context.getBean(ServerProperties.class);
        ConsulProperties consulProperties = context.getBean(ConsulProperties.class);
        AgentClient agentClient = context.getBean(AgentClient.class);

        String serviceName = consulProperties.getServiceName();

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
