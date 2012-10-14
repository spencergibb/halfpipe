package thirtytwo.degrees.halfpipe.configuration;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.netflix.config.*;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.util.ReflectionUtils.*;
/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:19 PM
 * TODO: json config http://svn.apache.org/viewvc/commons/proper/configuration/branches/configuration2_experimental/src/main/java/org/apache/commons/configuration2/
 */
public class ConfigurationBuilder {


    public interface PropBuilder<P, T> {
        static final DynamicPropertyFactory PROPS = DynamicPropertyFactory.getInstance();
        Class<P> getPropType();
        T defaultVal();
        T convert(String s);
        P getProp(String propName, T defaultVal);
    }

    class StringBuilder implements PropBuilder<DynamicStringProperty, String> {
        public Class<DynamicStringProperty> getPropType() { return DynamicStringProperty.class; }
        public String defaultVal() { return null; }
        public String convert(String s) { return s; }

        public DynamicStringProperty getProp(String propName, String defaultVal) {
            return PROPS.getStringProperty(propName, defaultVal);
        }
    }

    class IntBuilder implements PropBuilder<DynamicIntProperty, Integer> {
        public Class<DynamicIntProperty> getPropType() { return DynamicIntProperty.class; }
        public Integer defaultVal() { return 0; }
        public Integer convert(String s) { return Integer.parseInt(s); }

        public DynamicIntProperty getProp(String propName, Integer defaultVal) {
            return PROPS.getIntProperty(propName, defaultVal);
        }
    }

    class BooleanBuilder implements PropBuilder<DynamicBooleanProperty, Boolean> {
        public Class<DynamicBooleanProperty> getPropType() { return DynamicBooleanProperty.class; }
        public Boolean defaultVal() { return false; }
        public Boolean convert(String s) { return Boolean.parseBoolean(s); }

        public DynamicBooleanProperty getProp(String propName, Boolean defaultVal) {
            return PROPS.getBooleanProperty(propName, defaultVal);
        }
    }

    class LongBuilder implements PropBuilder<DynamicLongProperty, Long> {
        public Class<DynamicLongProperty> getPropType() { return DynamicLongProperty.class; }
        public Long defaultVal() { return 0L; }
        public Long convert(String s) { return Long.parseLong(s); }

        public DynamicLongProperty getProp(String propName, Long defaultVal) {
            return PROPS.getLongProperty(propName, defaultVal);
        }
    }

    class FloatBuilder implements PropBuilder<DynamicFloatProperty, Float> {
        public Class<DynamicFloatProperty> getPropType() { return DynamicFloatProperty.class; }
        public Float defaultVal() { return 0.0f; }
        public Float convert(String s) { return Float.parseFloat(s); }

        public DynamicFloatProperty getProp(String propName, Float defaultVal) {
            return PROPS.getFloatProperty(propName, defaultVal);
        }
    }

    class DoubleBuilder implements PropBuilder<DynamicDoubleProperty, Double> {
        public Class<DynamicDoubleProperty> getPropType() { return DynamicDoubleProperty.class; }
        public Double defaultVal() { return 0.0d; }
        public Double convert(String s) { return Double.parseDouble(s); }

        public DynamicDoubleProperty getProp(String propName, Double defaultVal) {
            return PROPS.getDoubleProperty(propName, defaultVal);
        }
    }

    protected List<PropBuilder<?, ?>> builders = Lists.newArrayList();

    public ConfigurationBuilder() {
        builders.add(new StringBuilder());
        builders.add(new IntBuilder());
        builders.add(new BooleanBuilder());
        builders.add(new LongBuilder());
        builders.add(new FloatBuilder());
        builders.add(new DoubleBuilder());
    }

    public void build(Object config) throws Exception {
        //TODO: validate
        build(config, "");
    }

    @SuppressWarnings("unchecked")
    protected void build(final Object config, final String context) throws Exception {
        Class<?> configClass = config.getClass();

        doWithFields(configClass, new FieldCallback(){
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Class<?> type = field.getType();
                String propName = getPropName(field, context);

                boolean fieldSet = false;
                for (PropBuilder propBuilder: builders) {
                    if (propBuilder.getPropType().isAssignableFrom(type)) {
                        makeAccessible(field);
                        Object defaultValue;
                        //TODO: use spring converters
                        DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                        if (annotation != null) {
                            defaultValue = propBuilder.convert(annotation.value());
                        } else {
                            defaultValue = propBuilder.defaultVal();
                        }

                        field.set(config, propBuilder.getProp(propName, defaultValue));

                        fieldSet = true;
                        break;
                    }
                }

                if (!fieldSet && field.get(config) == null) {
                    try {
                        Object fieldConfig = type.newInstance();
                        build(fieldConfig, propName);
                        field.set(config, fieldConfig);
                    } catch (Exception e) {
                        Throwables.propagate(e);
                    }
                }
            }});
    }

    protected String getPropName(Field field, String context) {
        if (StringUtils.isBlank(context))
            return field.getName();

        return context+"."+field.getName();
    }
}
