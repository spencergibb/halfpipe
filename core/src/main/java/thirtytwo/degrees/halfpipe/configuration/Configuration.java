package thirtytwo.degrees.halfpipe.configuration;

import com.netflix.config.DynamicStringProperty;
import thirtytwo.degrees.halfpipe.config.DefaultAppConfg;
import thirtytwo.degrees.halfpipe.configuration.HttpConfiguration;

import javax.ws.rs.DefaultValue;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:14 PM
 */
public class Configuration {
    @DefaultValue("Halfpipe")
    public DynamicStringProperty appName;

    @DefaultValue("halfpipebanner.txt")
    public DynamicStringProperty bannerFile;

    public HttpConfiguration http;

    public Class<?> appConfigClass = DefaultAppConfg.class;
}
