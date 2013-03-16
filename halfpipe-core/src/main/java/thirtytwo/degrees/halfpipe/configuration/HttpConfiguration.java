package thirtytwo.degrees.halfpipe.configuration;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.netflix.config.*;

import javax.ws.rs.DefaultValue;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
@PropertyCallback(HttpConfiguration.Callback.class)
public class HttpConfiguration {

    @PropertyCallback(PortCallback.class)
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

    public static class PortCallback extends AbstractCallback<HttpConfiguration, Integer> {
        @Override
        public void run() {
            System.err.println("http port changed to "+prop.getValue());
        }
    }

    public static class Callback extends AbstractCallback<HttpConfiguration, Object> {
        @Override
        public void run() {
            System.err.println("property named: "+prop.getName()+" changed to "+prop.getValue());
        }
    }
}
