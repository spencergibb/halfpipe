package halfpipe.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 1:30 PM
 */
public class ArchaiusApplicationListener implements SmartApplicationListener {
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
        System.out.println("ArchaiusApplicationListener received "+event);

        String url = null;
        String[] args = event.getArgs();
        if (args != null && args.length > 0) {
            url = args[0];
        }

        if (url == null) {
            String urls = event.getEnvironment().getProperty("archaius.urls");
            if (urls != null)
                System.setProperty("archaius.configurationSource.additionalUrls", urls);
        } else {
            System.setProperty("archaius.configurationSource.additionalUrls", url);
        }

        //TODO: configurable?
        System.setProperty("archaius.configurationSource.defaultFileName", "application.properties");

    }

    @Override
    public int getOrder() {
        return order;
    }
}
