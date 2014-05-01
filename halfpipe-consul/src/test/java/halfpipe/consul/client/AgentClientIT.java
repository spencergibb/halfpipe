package halfpipe.consul.client;

import com.google.common.collect.Lists;
import halfpipe.core.HalfpipeAutoConfig;
import halfpipe.consul.ConsulAutoConfig;
import halfpipe.consul.TestConfiguration;
import halfpipe.consul.model.Check;
import halfpipe.consul.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

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
public class AgentClientIT extends AbstractTestNGSpringContextTests {

    @Autowired
    AgentClient client;

    @Test
    public void testRegisterService() {
        Service service = new Service();
        service.setId("test1id");
        service.setName("test1Name");
        service.setPort(9999);
        service.setTags(Lists.newArrayList("test1tag1", "test1tag2"));
        Check check = new Check();
        check.setScript("/usr/local/bin/gtrue");
        check.setInterval(60);
        service.setCheck(check);
        client.register(service);
    }

    @Test(dependsOnMethods = "testRegisterService")
    public void testGetServices() {
        Map<String, Service> services = client.getServices();
        assertNotNull(services, "services was null");
        assertFalse(services.isEmpty(), "services was empty");
    }

    @Test(dependsOnMethods = "testGetServices")
    public void testDeregisterService() {
        client.deregister("test1id");
    }
}
