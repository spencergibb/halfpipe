package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import com.netflix.config.DynamicStringProperty;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:14 PM
 */
public class Configuration {
    public DynamicStringProperty appName = prop("Halfpipe");

    public DynamicStringProperty bannerFile = prop("halfpipebanner.txt");

    public DynamicStringProperty resourcePackages;

    public HttpConfiguration http;

    public LoggingConfiguration logging;
}
