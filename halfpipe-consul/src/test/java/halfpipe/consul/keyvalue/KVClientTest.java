package halfpipe.consul.keyvalue;

import halfpipe.client.ClientConfigurer;
import halfpipe.config.HalfpipeAutoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@SpringApplicationConfiguration(classes = {TestConfiguration.class,
        HalfpipeAutoConfig.class,
        KVClientTest.TestContext.class})
public class KVClientTest extends AbstractTestNGSpringContextTests {

    @Configuration
    public static class TestContext extends ClientConfigurer {
        @Bean
        public KVClient kvClient() {
            return builder().target(KVClient.class, "http://localhost:8500");
        }
    }

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