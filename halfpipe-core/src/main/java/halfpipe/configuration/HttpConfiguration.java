package halfpipe.configuration;

import static halfpipe.Halfpipe.*;
import static halfpipe.configuration.Defaults.*;

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

    public DynaProp<String> viewPattern = prop(ROOT_URL_PATTERN);

    public DynaProp<String> resourcePattern = prop(RESOURCE_URL_PATTERN);

    @PortRange
    @PropertyCallback(PortCallback.class)
    public DynaProp<Integer> port = prop(8080);

    @PortRange
    public DynaProp<Integer> adminPort = prop(8080); //TODO: implement admin port

    @Min(2)
    @Max(1000000)
    public DynaProp<Integer> maxThreads = prop(1024);

    @Min(1)
    @Max(1000000)
    public DynaProp<Integer> minThreads = prop(8);

    @NotNull
    public DynaProp<ConnectorType> connectorType = prop(ConnectorType.BLOCKING);

    @NotNull
    public DynaProp<Duration> maxIdleTime = prop(Duration.seconds(200));

    @Min(1)
    @Max(128)
    public DynaProp<Integer> acceptorThreads = prop(1);

    @Min(-Thread.NORM_PRIORITY)
    @Max(Thread.NORM_PRIORITY)
    public DynaProp<Integer> acceptorThreadPriorityOffset = prop(0);

    @Min(-1)
    public DynaProp<Integer> acceptQueueSize = prop(-1);

    @Min(1)
    public DynaProp<Integer> maxBufferCount = prop(1024);

    @NotNull
    public DynaProp<Size> requestBufferSize = prop(Size.kilobytes(16));

    @NotNull
    public DynaProp<Size> requestHeaderBufferSize = prop(Size.kilobytes(6));

    @NotNull
    public DynaProp<Size> responseBufferSize = prop(Size.kilobytes(32));

    @NotNull
    public DynaProp<Size> responseHeaderBufferSize = prop(Size.kilobytes(6));

    public DynaProp<Boolean> reuseAddress = prop(true);

    public DynaProp<Duration> soLingerTime;

    public DynaProp<Integer> lowResourcesConnectionThreshold = prop(0);

    @NotNull
    public DynaProp<Duration> lowResourcesMaxIdleTime = prop(Duration.seconds(0));

    @NotNull
    public DynaProp<Duration> shutdownGracePeriod = prop(Duration.seconds(2));

    public DynaProp<Boolean> useServerHeader = prop(false);

    public DynaProp<Boolean> useDateHeader = prop(true);

    public DynaProp<Boolean> useForwardedHeaders = prop(true);

    public DynaProp<Boolean> useDirectBuffers = prop(true);

    public DynaProp<String> bindHost;

    public DynaProp<String> adminUsername;

    public DynaProp<String> adminPassword;

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
