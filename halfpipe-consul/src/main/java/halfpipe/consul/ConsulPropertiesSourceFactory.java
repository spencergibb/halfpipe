package halfpipe.consul;

import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import halfpipe.properties.PropertiesSourceFactory;
import org.apache.commons.configuration.AbstractConfiguration;

import javax.inject.Inject;

/**
* User: spencergibb
* Date: 4/18/14
* Time: 11:01 AM
*/
public class ConsulPropertiesSourceFactory implements PropertiesSourceFactory {
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
