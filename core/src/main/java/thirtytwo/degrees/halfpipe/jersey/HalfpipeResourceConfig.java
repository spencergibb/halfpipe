package thirtytwo.degrees.halfpipe.jersey;

import com.sun.jersey.api.core.*;
import com.yammer.metrics.jersey.InstrumentedResourceMethodDispatchAdapter;
import org.springframework.stereotype.Component;

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
        //Not needed with spring metrics
        //getClasses().add(InstrumentedResourceMethodDispatchAdapter.class);
    }
}
