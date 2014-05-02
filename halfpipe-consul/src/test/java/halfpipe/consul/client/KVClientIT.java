package halfpipe.consul.client;

import halfpipe.client.ClientAutoConfig;
import halfpipe.consul.ConsulAutoConfig;
import halfpipe.consul.TestConfiguration;
import halfpipe.consul.model.KeyValue;
import halfpipe.core.HalfpipeAutoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@SpringApplicationConfiguration(classes = {
        ConsulAutoConfig.class,
        HalfpipeAutoConfig.class,
        ClientAutoConfig.class,
        TestConfiguration.class,
})
public class KVClientIT extends AbstractTestNGSpringContextTests {

    @Autowired
    KVClient kvClient;

    static {
        System.setProperty("application.id", "testId");
    }

    @Test
    public void testPut() {
        String key = "test/testkey";
        String value = "TestPut." + System.currentTimeMillis();

        boolean actual = kvClient.put(key, value);
        assertTrue(actual, "Invalid resposne");
    }

    @Test(dependsOnMethods = "testPut")
    public void testGetRecurse() {
        List<KeyValue> values = kvClient.getKeyValueRecurse();
        assertNotNull(values, "values is null");
        assertFalse(values.isEmpty(), "Values is null");
    }

    @Test(dependsOnMethods = "testGetRecurse")
    public void testDelete() {
        kvClient.delete("test/testkey");
    }
}