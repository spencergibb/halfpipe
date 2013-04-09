package halfpipe.configuration;

import static halfpipe.Halfpipe.*;
import static halfpipe.configuration.Defaults.*;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicStringProperty;
import halfpipe.util.Duration;
import halfpipe.util.Size;
import halfpipe.validation.PortRange;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @Valid
    @NotNull
    public RequestLogConfiguration requestLog;

    @Valid
    @NotNull
    public GzipConfiguration gzip;

    @Valid
    public SslConfiguration ssl;

    public DynamicStringProperty viewPattern = prop(ROOT_URL_PATTERN);

    public DynamicStringProperty resourcePattern = prop(RESOURCE_URL_PATTERN);

    @PortRange
    @PropertyCallback(PortCallback.class)
    public DynamicIntProperty port = prop(8080);

    @PortRange
    public DynamicIntProperty adminPort = prop(8080); //TODO: implement admin port

    @Min(2)
    @Max(1000000)
    public DynamicIntProperty maxThreads = prop(1024);

    @Min(1)
    @Max(1000000)
    public DynamicIntProperty minThreads = prop(8);

    @NotNull
    public DynamicProp<ConnectorType> connectorType = prop(ConnectorType.BLOCKING);

    @NotNull
    public DynamicProp<Duration> maxIdleTime = prop(Duration.seconds(200));

    @Min(1)
    @Max(128)
    public DynamicIntProperty acceptorThreads = prop(1);

    @Min(-Thread.NORM_PRIORITY)
    @Max(Thread.NORM_PRIORITY)
    public DynamicIntProperty acceptorThreadPriorityOffset = prop(0);

    @Min(-1)
    public DynamicIntProperty acceptQueueSize = prop(-1);

    @Min(1)
    public DynamicIntProperty maxBufferCount = prop(1024);

    @NotNull
    public DynamicProp<Size> requestBufferSize = prop(Size.kilobytes(16));

    @NotNull
    public DynamicProp<Size> requestHeaderBufferSize = prop(Size.kilobytes(6));

    @NotNull
    public DynamicProp<Size> responseBufferSize = prop(Size.kilobytes(32));

    @NotNull
    public DynamicProp<Size> responseHeaderBufferSize = prop(Size.kilobytes(6));

    public DynamicBooleanProperty reuseAddress = prop(true);

    public DynamicProp<Duration> soLingerTime;

    public DynamicIntProperty lowResourcesConnectionThreshold = prop(0);

    @NotNull
    public DynamicProp<Duration> lowResourcesMaxIdleTime = prop(Duration.seconds(0));

    @NotNull
    public DynamicProp<Duration> shutdownGracePeriod = prop(Duration.seconds(2));

    public DynamicBooleanProperty useServerHeader = prop(false);

    public DynamicBooleanProperty useDateHeader = prop(true);

    public DynamicBooleanProperty useForwardedHeaders = prop(true);

    public DynamicBooleanProperty useDirectBuffers = prop(true);

    public DynamicStringProperty bindHost;

    public DynamicStringProperty adminUsername;

    public DynamicStringProperty adminPassword;

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
