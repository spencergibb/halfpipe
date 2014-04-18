package halfpipe.consul.client;

import feign.RequestLine;
import halfpipe.consul.model.KeyValue;

import javax.inject.Named;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:22 PM
 */
public interface KVClient {
    @RequestLine("GET /v1/kv/{key}")
    List<KeyValue> getKeyValue(@Named("key") String key);

    @RequestLine("GET /v1/kv/?recurse=true")
    List<KeyValue> getKeyValueRecurse();

    @RequestLine("PUT /v1/kv/{key}")
    boolean put(@Named("key")String key, String value);

    @RequestLine("DELETE /v1/kv/{key}")
    void delete(@Named("key")String key);

}
