package thirtytwo.degrees.halfpipe.configuration;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicStringProperty;

import javax.ws.rs.DefaultValue;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
public class HttpConfiguration {
    @DefaultValue("8080")
    public DynamicIntProperty port;

    @DefaultValue(ROOT_URL_PATTERN)
    public DynamicStringProperty viewPattern;

    @DefaultValue(RESOURCE_URL_PATTERN)
    public DynamicStringProperty resourcePattern;

    /**
     * HTTP/1.1 or org.apache.coyote.http11.Http11NioProtocol
     */
    public DynamicStringProperty protocol;

    @DefaultValue("ISO-8859-1")
    public DynamicStringProperty uriEncoding;

    public GzipConfiguration gzip;
}
