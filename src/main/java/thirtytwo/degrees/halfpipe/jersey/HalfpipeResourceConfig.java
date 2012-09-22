package thirtytwo.degrees.halfpipe.jersey;

import com.sun.jersey.api.core.ScanningResourceConfig;
import com.yammer.metrics.jersey.InstrumentedResourceMethodDispatchAdapter;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 10:43 PM
 */
public class HalfpipeResourceConfig extends ScanningResourceConfig {

    public HalfpipeResourceConfig() {
        getClasses().add(InstrumentedResourceMethodDispatchAdapter.class);
    }
}
