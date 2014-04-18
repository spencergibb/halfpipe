package halfpipe.properties;

import org.apache.commons.configuration.AbstractConfiguration;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 11:02 PM
 */
public interface PropertiesSourceFactory {
    public String getName();
    public AbstractConfiguration getConfiguration();
}
