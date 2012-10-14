package thirtytwo.degrees.halfpipe.configuration;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.netflix.config.*;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.util.ReflectionUtils.*;
/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:19 PM
 * TODO: json config http://svn.apache.org/viewvc/commons/proper/configuration/branches/configuration2_experimental/src/main/java/org/apache/commons/configuration2/
 */
public class ConfigurationBuilder {

    public static abstract class PropBuilder<P, T> {
        abstract Class<P> getPropType();
        abstract T defaultVal();
        abstract T convert(String s, Class<?> valueClass) throws Exception;
        abstract P getProp(String propName, T defaultVal, Class<?> valueClass);
        DynamicPropertyFactory props() {
            return DynamicPropertyFactory.getInstance();
        }
    }

    class StringBuilder extends PropBuilder<DynamicStringProperty, String> {
        public Class<DynamicStringProperty> getPropType() { return DynamicStringProperty.class; }
        public String defaultVal() { return null; }
        public String convert(String s, Class<?> valueClass) { return s; }

        public DynamicStringProperty getProp(String propName, String defaultVal, Class<?> valueClass) {
            return props().getStringProperty(propName, defaultVal);
        }
    }

    class IntBuilder extends PropBuilder<DynamicIntProperty, Integer> {
        public Class<DynamicIntProperty> getPropType() { return DynamicIntProperty.class; }
        public Integer defaultVal() { return Integer.MIN_VALUE; }
        public Integer convert(String s, Class<?> valueClass) { return Integer.parseInt(s); }

        public DynamicIntProperty getProp(String propName, Integer defaultVal, Class<?> valueClass) {
            return props().getIntProperty(propName, defaultVal);
        }
    }

    class BooleanBuilder extends PropBuilder<DynamicBooleanProperty, Boolean> {
        public Class<DynamicBooleanProperty> getPropType() { return DynamicBooleanProperty.class; }
        public Boolean defaultVal() { return false; }
        public Boolean convert(String s, Class<?> valueClass) { return Boolean.parseBoolean(s); }

        public DynamicBooleanProperty getProp(String propName, Boolean defaultVal, Class<?> valueClass) {
            return props().getBooleanProperty(propName, defaultVal);
        }
    }

    class LongBuilder extends PropBuilder<DynamicLongProperty, Long> {
        public Class<DynamicLongProperty> getPropType() { return DynamicLongProperty.class; }
        public Long defaultVal() { return Long.MIN_VALUE; }
        public Long convert(String s, Class<?> valueClass) { return Long.parseLong(s); }

        public DynamicLongProperty getProp(String propName, Long defaultVal, Class<?> valueClass) {
            return props().getLongProperty(propName, defaultVal);
        }
    }

    class FloatBuilder extends PropBuilder<DynamicFloatProperty, Float> {
        public Class<DynamicFloatProperty> getPropType() { return DynamicFloatProperty.class; }
        public Float defaultVal() { return Float.NaN; }
        public Float convert(String s, Class<?> valueClass) { return Float.parseFloat(s); }

        public DynamicFloatProperty getProp(String propName, Float defaultVal, Class<?> valueClass) {
            return props().getFloatProperty(propName, defaultVal);
        }
    }

    class DoubleBuilder extends PropBuilder<DynamicDoubleProperty, Double> {
        public Class<DynamicDoubleProperty> getPropType() { return DynamicDoubleProperty.class; }
        public Double defaultVal() { return Double.NaN; }
        public Double convert(String s, Class<?> valueClass) { return Double.parseDouble(s); }

        public DynamicDoubleProperty getProp(String propName, Double defaultVal, Class<?> valueClass) {
            return props().getDoubleProperty(propName, defaultVal);
        }
    }

    @SuppressWarnings("unchecked")
    class GenericBuilder extends PropBuilder<DynamicProp, Object> {
        Class<DynamicProp> getPropType() {
            return (Class<DynamicProp>) DynamicProp.class;
        }

        Object defaultVal() { return null; }

        Object convert(String s, Class<?> valueClass) throws Exception {
            return DynamicProp.convert(valueClass, s);
        }

        DynamicProp<Object> getProp(String propName, Object defaultVal, Class<?> valueClass) {
            return new DynamicProp<Object>(propName, defaultVal, valueClass);
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
        builders.add(new GenericBuilder());
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
                        Object defaultValue = null;
                        //TODO: use spring converters
                        DefaultValue annotation = field.getAnnotation(DefaultValue.class);
                        Class<?> valueClass = DynamicProp.getValueClass(field.getGenericType());
                        if (annotation != null) {
                            try {
                                defaultValue = propBuilder.convert(annotation.value(), valueClass);
                            } catch (Exception e) {
                                Throwables.propagate(e);
                            }
                        } else {
                            defaultValue = propBuilder.defaultVal();
                        }

                        field.set(config, propBuilder.getProp(propName, defaultValue, valueClass));

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
