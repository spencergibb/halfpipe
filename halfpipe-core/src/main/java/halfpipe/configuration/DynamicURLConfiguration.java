package halfpipe.configuration;

import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;

/**
 * A {@link com.netflix.config.DynamicConfiguration} that uses a {@link com.netflix.config.sources.URLConfigurationSource} and
 * {@link com.netflix.config.FixedDelayPollingScheduler}.
 *
 */
@SuppressWarnings("unchecked")
public class DynamicURLConfiguration extends DynamicConfiguration {

    /**
     * Create an instance with default {@link com.netflix.config.sources.URLConfigurationSource#URLConfigurationSource()} and
     * {@link com.netflix.config.FixedDelayPollingScheduler#FixedDelayPollingScheduler()} and start polling the source
     * if there is any URLs available for polling.
     */
    public DynamicURLConfiguration() {
        URLConfigurationSource source = new URLConfigurationSource();
        if (source.getConfigUrls() != null && source.getConfigUrls().size() > 0) {
            startPolling(source, new FixedDelayPollingScheduler());
        }
    }

    public DynamicURLConfiguration(String... urls) {
        URLConfigurationSource source = new URLConfigurationSource(urls);
        if (source.getConfigUrls() != null && source.getConfigUrls().size() > 0) {
            startPolling(source, new FixedDelayPollingScheduler());
        }
    }

    /**
     * Create an instance and start polling the source.
     *
     * @param initialDelayMillis initial delay in milliseconds used by {@link FixedDelayPollingScheduler}
     * @param delayMillis delay interval in milliseconds used by {@link FixedDelayPollingScheduler}
     * @param ignoreDeletesFromSource whether the scheduler should ignore deletes of properties from configuration source when
     * applying the polling result to a configuration.
     * @param urls The set of URLs to be polled by {@link URLConfigurationSource}
     */
    public DynamicURLConfiguration(int initialDelayMillis, int delayMillis, boolean ignoreDeletesFromSource,
                                   String... urls) {
        super(new URLConfigurationSource(urls),
                new FixedDelayPollingScheduler(initialDelayMillis, delayMillis, ignoreDeletesFromSource));
    }
}
