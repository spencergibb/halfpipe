package thirtytwo.degrees.halfpipe.configuration;

import com.netflix.config.*;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.Field;

import static org.springframework.util.ReflectionUtils.*;
/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:19 PM
 * TODO: json config http://svn.apache.org/viewvc/commons/proper/configuration/branches/configuration2_experimental/src/main/java/org/apache/commons/configuration2/
 */
public class ConfigurationFactory {
    public static void build(Object config) throws Exception {
        //TODO: validate
        build(config, "");
    }
    protected static void build(final Object config, final String context) throws Exception {
        Class<?> configClass = config.getClass();

        doWithFields(configClass, new FieldCallback(){
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Class<?> type = field.getType();
                String propName = getPropName(field, context);
                DynamicPropertyFactory props = DynamicPropertyFactory.getInstance();
                if (DynamicStringProperty.class.isAssignableFrom(type)) {
                    String defaultValue;
                    //TODO: use spring converters
                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = annotation.value();
                    } else {
                        defaultValue = "";
                    }

                    field.set(config, props.getStringProperty(propName, defaultValue));
                } else if (DynamicIntProperty.class.isAssignableFrom(type)) {
                    int defaultValue;

                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = Integer.parseInt(annotation.value());
                    } else {
                        defaultValue = 0;
                    }
                    field.set(config, props.getIntProperty(propName, defaultValue));
                } else if (DynamicBooleanProperty.class.isAssignableFrom(type)) {
                    boolean defaultValue;

                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = Boolean.parseBoolean(annotation.value());
                    } else {
                        defaultValue = false;
                    }
                    field.set(config, props.getBooleanProperty(propName, defaultValue));
                } else if (DynamicLongProperty.class.isAssignableFrom(type)) {
                    long defaultValue;

                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = Long.parseLong(annotation.value());
                    } else {
                        defaultValue = 0;
                    }
                    field.set(config, props.getLongProperty(propName, defaultValue));
                } else if (DynamicFloatProperty.class.isAssignableFrom(type)) {
                    float defaultValue;

                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = Float.parseFloat(annotation.value());
                    } else {
                        defaultValue = 0.0f;
                    }
                    field.set(config, props.getFloatProperty(propName, defaultValue));
                } else if (DynamicDoubleProperty.class.isAssignableFrom(type)) {
                    double defaultValue;

                    DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                    if (annotation != null) {
                        defaultValue = Double.parseDouble(annotation.value());
                    } else {
                        defaultValue = 0.0d;
                    }
                    field.set(config, props.getDoubleProperty(propName, defaultValue));
                } else {
                    if (field.get(config) == null) {
                        try {
                            Object fieldConfig = type.newInstance();
                            build(fieldConfig, propName);
                            field.set(config, fieldConfig);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }});
    }

    private static String getPropName(Field field, String context) {
        if (StringUtils.isBlank(context))
            return field.getName();

        return context+"."+field.getName();
    }
}
