package halfpipe.jersey;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.metrics.jersey.InstrumentedResourceMethodDispatchAdapter;

import java.util.Map;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 10:43 PM
 */
public class HalfpipeResourceConfig extends PackagesResourceConfig {

    public HalfpipeResourceConfig(Map<String, Object> props) {
        super(props);

        setPropertiesAndFeatures(props);
        getFeatures().put(ResourceConfig.FEATURE_DISABLE_WADL, Boolean.TRUE);
        getClasses().add(InstrumentedResourceMethodDispatchAdapter.class);
    }
}
