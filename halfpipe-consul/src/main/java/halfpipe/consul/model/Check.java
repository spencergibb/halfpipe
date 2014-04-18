package halfpipe.consul.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 10:59 AM
 */
@Data
public class Check {
    @JsonProperty("Script")
    private String script;

    @JsonProperty("Interval")
    private int interval;

    @JsonProperty("TTL")
    private int ttl;
}
