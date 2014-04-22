package halfpipe.consul.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 10:50 AM
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceNode {
    @JsonProperty("Node")
    private String node;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("ServiceID")
    private String serviceID;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("ServiceTags")
    private List<String> serviceTags;

    @JsonProperty("ServicePort")
    private int servicePort;
}
