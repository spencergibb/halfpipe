package thirtytwo.degrees.halfpipe;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.google.common.collect.Maps;
import com.netflix.config.*;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Map;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:13 PM
 */
public class HalfpipeConfiguration {

    public static void createConfig(boolean installDefaultServlet) {
        if (DynamicPropertyFactory.isInitializedWithDefaultConfig() || ConfigurationManager.isConfigurationInstalled()) {
            System.err.println("WARNING!!! Trying to initialze config again");
            return; //TODO: why does this happen on exploded?
        }

        System.setProperty("archaius.configurationSource.defaultFileName", HALFPIPE_PROPERTIES_FILENAME);

        Map<String, Object> map = Maps.newHashMap();
        map.put(PROP_INSTALL_DEFAULT_SERVLET, installDefaultServlet);
        System.err.println("Config: "+map);

        ConcurrentCompositeConfiguration configuration = new ConcurrentCompositeConfiguration();
        configuration.addConfiguration(new ConcurrentMapConfiguration(map));
        configuration.addConfiguration(new SystemConfiguration(), DynamicPropertyFactory.SYS_CONFIG_NAME);
        //configuration.addConfiguration(new ConcurrentMapConfiguration(ClasspathPropertiesConfiguration.));
        try {
            DynamicURLConfiguration defaultURLConfig = new DynamicURLConfiguration();
            configuration.addConfiguration(defaultURLConfig, DynamicPropertyFactory.URL_CONFIG_NAME);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        DynamicPropertyFactory.initWithConfigurationSource(configuration);
    }

    public static ApplicationContext createContext(String classConfigProperty) throws ClassNotFoundException {
        Class<?> appConfigClass = getConfigClass(classConfigProperty);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(appConfigClass);
        return ctx;
    }

    public static Class<?> getConfigClass(String classConfigProperty) throws ClassNotFoundException {
        DynamicStringProperty className = getStringProp(classConfigProperty);

        return Class.forName(className.get());
    }

    public static DynamicStringProperty getStringProp(String propName) {
        DynamicStringProperty prop = getStringProp(propName, null);

        Assert.hasText(prop.get(), propName + " must not be empty");
        return prop;
    }

    public static DynamicStringProperty getStringProp(String propName, String defaultValue) {
        return DynamicPropertyFactory.getInstance().getStringProperty(propName, defaultValue);
    }

}
