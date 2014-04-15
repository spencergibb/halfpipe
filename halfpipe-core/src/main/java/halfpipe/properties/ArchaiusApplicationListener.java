package halfpipe.properties;

import static com.netflix.config.ConfigurationManager.*;

import com.netflix.config.*;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 1:30 PM
 */
public class ArchaiusApplicationListener implements SmartApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(ArchaiusApplicationListener.class);
    private final int order = 0;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return SpringApplication.class.isAssignableFrom(sourceType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        ApplicationEnvironmentPreparedEvent event = (ApplicationEnvironmentPreparedEvent) applicationEvent;
        logger.debug("ArchaiusApplicationListener received " + event);
        ConfigurableEnvironment env = event.getEnvironment();

        String url = null;
        String[] args = event.getArgs();
        if (args != null && args.length > 0) {
            url = args[0];
        }

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
        try {
            FixedDelayPollingScheduler pollingScheduler = new FixedDelayPollingScheduler();
            UrlPropertiesSource propertiesSource = new UrlPropertiesSource();
            DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(propertiesSource, pollingScheduler);
            config.addConfiguration(dynamicConfiguration, URL_CONFIG_NAME);
        } catch (Throwable e) {
            logger.warn("Failed to create default dynamic configuration", e);
        }

        //TODO: add support to add other Configurations (Jdbc, DynamoDb, Zookeeper, jclouds, etc...)

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

    @Override
    public int getOrder() {
        return order;
    }
}
