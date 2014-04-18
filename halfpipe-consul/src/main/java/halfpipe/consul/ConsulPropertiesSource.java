package halfpipe.consul;

import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.KeyValue;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 10:56 PM
 * TODO: use consul blocking calls to implement WatchedConfigurationSource?
 */
public class ConsulPropertiesSource implements PolledConfigurationSource {

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
