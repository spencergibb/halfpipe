package halfpipe.consul.client;

import halfpipe.core.HalfpipeAutoConfig;
import halfpipe.consul.ConsulAutoConfig;
import halfpipe.consul.TestConfiguration;
import halfpipe.consul.model.ServiceNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 11:04 AM
 */
@SpringApplicationConfiguration(classes = {TestConfiguration.class,
        ConsulAutoConfig.class,
        HalfpipeAutoConfig.class
})
public class CatalogClientIT extends AbstractTestNGSpringContextTests {

    @Autowired
    CatalogClient client;

    @Test
    public void testGetServices() {
        Map<String, List<String>> services = client.getServices();
        assertNotNull(services, "services is null");
        assertTrue(services.containsKey("consul"), "No consul key");
    }

    @Test
    public void testGetService() {
        List<ServiceNode> serviceNodes = client.getServiceNodes("consul");
        assertNotNull(serviceNodes, "serviceNodes is null");
        assertFalse(serviceNodes.isEmpty(), "serviceNodes is empty");

        ServiceNode node = serviceNodes.get(0);

        assertNotNull(node.getAddress(), "address is null");
        assertNotNull(node.getNode(), "node is null");
        assertNotNull(node.getServiceID(), "serviceId is null");
        assertNotNull(node.getServiceName(), "serviceName is null");
        assertTrue(node.getServicePort() > 0, "servicePort is wrong");
    }
}
