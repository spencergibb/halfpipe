package halfpipe.consul.client;

import halfpipe.config.HalfpipeAutoConfig;
import halfpipe.consul.ConsulAutoConfig;
import halfpipe.consul.TestConfiguration;
import halfpipe.consul.model.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@SpringApplicationConfiguration(classes = {TestConfiguration.class,
        ConsulAutoConfig.class,
        HalfpipeAutoConfig.class
})
public class KVClientIT extends AbstractTestNGSpringContextTests {

    @Autowired
    KVClient kvClient;

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