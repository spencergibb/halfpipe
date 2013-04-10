package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import com.netflix.config.DynamicStringProperty;

import javax.validation.Valid;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:14 PM
 */
public class Configuration {
    public DynaProp<String> appName = prop("Halfpipe");

    public DynaProp<String> bannerFile = prop("halfpipebanner.txt");

    public DynaProp<String> resourcePackages;

    @Valid
    public HttpConfiguration http;

    @Valid
    public LoggingConfiguration logging;
}
