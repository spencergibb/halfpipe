package halfpipe.consul;

import halfpipe.consul.client.AgentClient;
import halfpipe.consul.client.CatalogClient;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.KeyValue;
import halfpipe.consul.model.Service;
import halfpipe.consul.model.ServiceNode;
import halfpipe.web.EndpointDelegate;
import lombok.Data;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 5/1/14
 * Time: 3:30 PM
 */
public class ConsulEndpoint extends EndpointMvcAdapter {

    @Inject
    KVClient kvClient;

    @Inject
    CatalogClient catalogClient;

    @Inject
    AgentClient agentClient;

    @Inject
    public ConsulEndpoint(ConsulEndpointProps properties) {
        super(new EndpointDelegate(properties));
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public Object invoke() {
        ConsulData data = new ConsulData();
        data.setKeyValues(kvClient.getKeyValueRecurse());
        data.setCatalogServices(catalogClient.getServices());
        Map<String, Service> services = agentClient.getServices();
        data.setAgentServices(services);

        for (String serviceId : services.keySet()) {
            data.getCatalogServiceNodes().put(serviceId, catalogClient.getServiceNodes(serviceId));
        }

        return data;
    }

    @Data
    public static class ConsulData {
        Map<String, List<String>> catalogServices;

        Map<String, List<ServiceNode>> catalogServiceNodes = new LinkedHashMap<>();

        Map<String, Service> agentServices;

        List<KeyValue> keyValues;
    }
}
