package halfpipe.client;

import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;

/**
 * User: spencergibb
 * Date: 4/16/14
 * Time: 11:53 AM
 */
@ArchaiusProperties("halfpipe.client")
@Data
public class ClientProperties {
    private DynaProp<String> defaultGroup;
}
