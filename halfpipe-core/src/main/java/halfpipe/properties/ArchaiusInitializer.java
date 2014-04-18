package halfpipe.properties;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.netflix.config.ConfigurationManager.*;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 1:30 PM
 */
public class ArchaiusInitializer /*implements SmartApplicationListener*/ {
    private static final Logger logger = LoggerFactory.getLogger(ArchaiusInitializer.class);

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Inject
    ConfigurableApplicationContext context;

    @Inject
    List<PropertiesSourceFactory> factories;

    public synchronized void initializeArchaius() {
        if (initialized.compareAndSet(false, true)) {
            //ApplicationPreparedEvent event = (ApplicationPreparedEvent) applicationEvent;
            //logger.debug("ArchaiusInitializer received " + event);
            ConfigurableEnvironment env = context.getEnvironment();

            String url = null;
        /*String[] args = event.getArgs();
        if (args != null && args.length > 0) {
            url = args[0];
        }*/

            if (url == null) {
                String urls = env.getProperty("archaius.urls");
                if (urls != null)
                    System.setProperty("archaius.configurationSource.additionalUrls", urls);
            } else {
                System.setProperty("archaius.configurationSource.additionalUrls", url);
            }

            String defaultFileName = env.getProperty("archauis.file.name", "application.yml");
            System.setProperty("archaius.configurationSource.defaultFileName", defaultFileName);

            ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();

            //TODO: add support to add other Configurations (Jdbc, DynamoDb, Zookeeper, jclouds, etc...)
            if (factories != null && !factories.isEmpty()) {
                for (PropertiesSourceFactory factory: factories) {
                    config.addConfiguration(factory.getConfiguration(), factory.getName());
                }
            }

            try {
                FixedDelayPollingScheduler pollingScheduler = new FixedDelayPollingScheduler();
                UrlPropertiesSource propertiesSource = new UrlPropertiesSource();
                DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(propertiesSource, pollingScheduler);
                config.addConfiguration(dynamicConfiguration, URL_CONFIG_NAME);
            } catch (Throwable e) {
                logger.warn("Failed to create default dynamic configuration", e);
            }

            if (!Boolean.getBoolean(DISABLE_DEFAULT_SYS_CONFIG)) {
                SystemConfiguration sysConfig = new SystemConfiguration();
                config.addConfiguration(sysConfig, SYS_CONFIG_NAME);
            }
            if (!Boolean.getBoolean(DISABLE_DEFAULT_ENV_CONFIG)) {
                EnvironmentConfiguration envConfig = new EnvironmentConfiguration();
                config.addConfiguration(envConfig, ENV_CONFIG_NAME);
            }

            ConcurrentCompositeConfiguration appOverrideConfig = new ConcurrentCompositeConfiguration();
            config.addConfiguration(appOverrideConfig, APPLICATION_PROPERTIES);
            config.setContainerConfigurationIndex(config.getIndexOfConfiguration(appOverrideConfig));

            ConfigurationManager.install(config);
        }
    }

    /*@Override
    public int getOrder() {
        return order;
    }*/
}
