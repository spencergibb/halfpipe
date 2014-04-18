package halfpipe.consul.client;

import feign.RequestLine;
import halfpipe.consul.model.Service;

import javax.inject.Named;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:22 PM
 */
public interface AgentClient {
    @RequestLine("GET /v1/agent/services")
    Map<String, Service> getServices();

    @RequestLine("PUT /v1/agent/service/register")
    void register(Service service);

    @RequestLine("PUT /v1/agent/service/deregister/{serviceId}")
    void deregister(@Named("serviceId") String serviceId);
}
