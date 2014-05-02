package halfpipe.consul.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import halfpipe.client.ClientAutoConfig;
import halfpipe.consul.ConsulAutoConfig;
import halfpipe.consul.TestConfiguration;
import halfpipe.consul.model.KeyValue;
import halfpipe.core.HalfpipeAutoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.*;

@SpringApplicationConfiguration(classes = {
        ConsulAutoConfig.class,
        HalfpipeAutoConfig.class,
        ClientAutoConfig.class,
        TestConfiguration.class,
})
public class KVClientIT extends AbstractTestNGSpringContextTests {

    public static final String KEY = "test/testkey";
    public static final String VALUE = "TestPut." + System.currentTimeMillis();

    @Autowired
    KVClient kvClient;

    @Autowired
    ObjectMapper objectMapper;

    static {
        System.setProperty("application.id", "testId");
    }


    @Test
    public void testPut() {

        boolean actual = kvClient.put(KEY, VALUE);
        assertTrue(actual, "Invalid resposne");
    }

    @Test(dependsOnMethods = "testPut")
    public void testGet() throws IOException {
        List<KeyValue> values = kvClient.getKeyValue(KEY);
        assertNotNull(values, "values is null");
        assertFalse(values.isEmpty(), "Values is null");
        assertTrue(values.size() == 1, "Values is not size 1");
        KeyValue keyValue = values.get(0);
        //TODO: how to deal with this?
        String decoded = objectMapper.readValue(keyValue.getDecoded(), String.class);

        assertEquals(decoded, VALUE);
    }

    @Test(dependsOnMethods = "testPut")
    public void testGetKeyRecurse() {
        List<KeyValue> values = kvClient.getKeyValueRecurse(KEY);
        assertNotNull(values, "values is null");
        assertFalse(values.isEmpty(), "Values is null");
    }

    @Test(dependsOnMethods = "testGetKeyRecurse")
    public void testGetRecurse() {
        List<KeyValue> values = kvClient.getKeyValueRecurse();
        assertNotNull(values, "values is null");
        assertFalse(values.isEmpty(), "Values is null");
    }

    @Test(dependsOnMethods = "testGetRecurse")
    public void testDelete() {
        kvClient.delete(KEY);
    }
}