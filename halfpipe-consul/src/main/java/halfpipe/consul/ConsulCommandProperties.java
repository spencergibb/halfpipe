package halfpipe.consul;

import halfpipe.client.HystrixCommandProperties;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 11:45 PM
 */
public class ConsulCommandProperties extends HystrixCommandProperties {

    @Inject
    ConsulProperties properties;

    @Override
    public String getDefaultGroup() {
        return properties.getServiceName();
    }
}
