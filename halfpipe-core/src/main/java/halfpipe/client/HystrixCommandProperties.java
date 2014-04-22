package halfpipe.client;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 11:43 PM
 */
public class HystrixCommandProperties {

    @Inject
    ClientProperties clientProps;

    public String getDefaultGroup() {
        return clientProps.getDefaultGroup();
    }
}
