package halfpipe.consul.client;

import feign.RequestLine;
import halfpipe.consul.model.ServiceNode;

import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:22 PM
 */
public interface CatalogClient {
    @RequestLine("GET /v1/catalog/services")
    Map<String, List<String>> getServices();

    @RequestLine("GET /v1/catalog/service/{serviceId}")
    List<ServiceNode> getServiceNodes(@Named("serviceId") String serviceId);
}
