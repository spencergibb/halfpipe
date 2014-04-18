package halfpipe.consul.keyvalue;

import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import halfpipe.properties.PropertiesSourceFactory;
import org.apache.commons.configuration.AbstractConfiguration;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 10:56 PM
 */
public class ConsulPropertiesSource implements PolledConfigurationSource {

    public static class Factory implements PropertiesSourceFactory {
        @Inject
        ConsulPropertiesSource propertiesSource;

        @Override
        public String getName() {
            return ConsulPropertiesSource.class.getSimpleName();
        }

        @Override
        public AbstractConfiguration getConfiguration() {
            //boolean consulEnabled = Boolean.parseBoolean(env.getProperty("halfpipe.consul.properties.enabled", "false"));
            FixedDelayPollingScheduler pollingScheduler = new FixedDelayPollingScheduler();
            DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(propertiesSource, pollingScheduler);
            return dynamicConfiguration;
        }
    }

    @Inject
    private KVClient client;

    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception {
        Map<String, Object> map = load();
        return PollResult.createFull(map);
    }

    synchronized Map<String, Object> load() throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<KeyValue> values = client.getKeyValueRecurse();

        for (KeyValue kv : values) {
            map.put(kv.getKey().replace('/', '.'), kv.getDecoded());
        }

        return map;
    }
}
