package halfpipe.configuration;

import static halfpipe.Halfpipe.*;
import static halfpipe.configuration.Defaults.*;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicStringProperty;
import halfpipe.util.Duration;
import halfpipe.validation.PortRange;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
@PropertyCallback(HttpConfiguration.Callback.class)
public class HttpConfiguration {

    public enum ConnectorType {
        BLOCKING,
        LEGACY,
        LEGACY_SSL,
        NONBLOCKING,
        NONBLOCKING_SSL;

        @Override
        public String toString() {
            return super.toString().replace("_", "+").toLowerCase(Locale.ENGLISH);
        }

        public static ConnectorType parse(String type) {
            return valueOf(type.toUpperCase(Locale.ENGLISH).replace('+', '_'));
        }
    }

    /*@Valid
    @NotNull
    public RequestLogConfiguration requestLog = new RequestLogConfiguration();*/

    @Valid
    @NotNull
    public GzipConfiguration gzip;

    /*@Valid
    public SslConfiguration ssl = null;*/

    public DynamicStringProperty viewPattern = prop(ROOT_URL_PATTERN);

    public DynamicStringProperty resourcePattern = prop(RESOURCE_URL_PATTERN);

    @PortRange
    @PropertyCallback(PortCallback.class)
    public DynamicIntProperty port = prop(8080);

/*
    @PortRange
    public int adminPort = 8081;

    @Min(2)
    @Max(1000000)
    public int maxThreads = 1024;

    @Min(1)
    @Max(1000000)
    public int minThreads = 8;

    @NotNull
    public ConnectorType connectorType = ConnectorType.BLOCKING;
*/
    @NotNull
    public DynamicProp<Duration> maxIdleTime = prop(Duration.seconds(200));
/*
    @Min(1)
    @Max(128)
    public int acceptorThreads = 1;

    @Min(-Thread.NORM_PRIORITY)
    @Max(Thread.NORM_PRIORITY)
    public int acceptorThreadPriorityOffset = 0;

    @Min(-1)
    public int acceptQueueSize = -1;

    @Min(1)
    public int maxBufferCount = 1024;

    @NotNull
    public Size requestBufferSize = Size.kilobytes(16);

    @NotNull
    public Size requestHeaderBufferSize = Size.kilobytes(6);

    @NotNull
    public Size responseBufferSize = Size.kilobytes(32);

    @NotNull
    public Size responseHeaderBufferSize = Size.kilobytes(6);

    public boolean reuseAddress = true;

    public Duration soLingerTime = null;

    public int lowResourcesConnectionThreshold = 0;

    @NotNull
    public Duration lowResourcesMaxIdleTime = Duration.seconds(0);

    @NotNull
    public Duration shutdownGracePeriod = Duration.seconds(2);

    public boolean useServerHeader = false;

    public boolean useDateHeader = true;

    public boolean useForwardedHeaders = true;

    public boolean useDirectBuffers = true;

    public String bindHost = null;

    public String adminUsername = null;

    public String adminPassword = null;
*/


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
