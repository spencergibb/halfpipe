package halfpipe.configuration;

import static org.springframework.util.ReflectionUtils.*;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.netflix.config.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.*;
import java.util.List;
/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:19 PM
 * TODO: json config http://svn.apache.org/viewvc/commons/proper/configuration/branches/configuration2_experimental/src/main/java/org/apache/commons/configuration2/
 */
public class ConfigurationBuilder {
    public static ConversionService conversions;

    public abstract class PropBuilder<P, T> {
        abstract Class<P> getPropType();
        abstract T defaultVal();

        @SuppressWarnings("unchecked")
        T convert(String s, Class<?> valueClass) throws Exception {
            if (conversionService.canConvert(s.getClass(), valueClass)) {
                return (T) conversionService.convert(s, valueClass);
            }
            throw new IllegalArgumentException("Unable to convert '"+s+" to type "+valueClass);
        }

        abstract P getProp(String propName, T defaultVal, Class<?> valueClass);

        DynamicPropertyFactory props() {
            return DynamicPropertyFactory.getInstance();
        }
    }

    class StringBuilder extends PropBuilder<DynamicStringProperty, String> {
        public Class<DynamicStringProperty> getPropType() { return DynamicStringProperty.class; }
        public String defaultVal() { return null; }

        public DynamicStringProperty getProp(String propName, String defaultVal, Class<?> valueClass) {
            return props().getStringProperty(propName, defaultVal);
        }
    }

    class IntBuilder extends PropBuilder<DynamicIntProperty, Integer> {
        public Class<DynamicIntProperty> getPropType() { return DynamicIntProperty.class; }
        public Integer defaultVal() { return Integer.MIN_VALUE; }

        public DynamicIntProperty getProp(String propName, Integer defaultVal, Class<?> valueClass) {
            return props().getIntProperty(propName, defaultVal);
        }
    }

    class BooleanBuilder extends PropBuilder<DynamicBooleanProperty, Boolean> {
        public Class<DynamicBooleanProperty> getPropType() { return DynamicBooleanProperty.class; }
        public Boolean defaultVal() { return false; }

        public DynamicBooleanProperty getProp(String propName, Boolean defaultVal, Class<?> valueClass) {
            return props().getBooleanProperty(propName, defaultVal);
        }
    }

    class LongBuilder extends PropBuilder<DynamicLongProperty, Long> {
        public Class<DynamicLongProperty> getPropType() { return DynamicLongProperty.class; }
        public Long defaultVal() { return Long.MIN_VALUE; }

        public DynamicLongProperty getProp(String propName, Long defaultVal, Class<?> valueClass) {
            return props().getLongProperty(propName, defaultVal);
        }
    }

    class FloatBuilder extends PropBuilder<DynamicFloatProperty, Float> {
        public Class<DynamicFloatProperty> getPropType() { return DynamicFloatProperty.class; }
        public Float defaultVal() { return Float.NaN; }

        public DynamicFloatProperty getProp(String propName, Float defaultVal, Class<?> valueClass) {
            return props().getFloatProperty(propName, defaultVal);
        }
    }

    class DoubleBuilder extends PropBuilder<DynamicDoubleProperty, Double> {
        public Class<DynamicDoubleProperty> getPropType() { return DynamicDoubleProperty.class; }
        public Double defaultVal() { return Double.NaN; }

        public DynamicDoubleProperty getProp(String propName, Double defaultVal, Class<?> valueClass) {
            return props().getDoubleProperty(propName, defaultVal);
        }
    }

    @SuppressWarnings("unchecked")
    class GenericBuilder extends PropBuilder<DynamicProp, Object> {
        Class<DynamicProp> getPropType() {
            return DynamicProp.class;
        }

        Object defaultVal() { return null; }

        DynamicProp<Object> getProp(String propName, Object defaultVal, Class<?> valueClass) {
            return new DynamicProp<Object>(propName, defaultVal, valueClass);
        }
    }

    protected List<PropBuilder<?, ?>> builders = Lists.newArrayList();

    ConversionService conversionService;

    public ConfigurationBuilder(ConversionService conversionService) {
        builders.add(new StringBuilder());
        builders.add(new IntBuilder());
        builders.add(new BooleanBuilder());
        builders.add(new LongBuilder());
        builders.add(new FloatBuilder());
        builders.add(new DoubleBuilder());
        builders.add(new GenericBuilder());
        this.conversionService = conversionService;
        conversions = conversionService;
    }

    public void build(Object config) throws Exception {
        //TODO: validate configuration
        build(config, "");
    }

    @SuppressWarnings("unchecked")
    protected void build(final Object config, final String context) throws Exception {
        Class<?> configClass = config.getClass();
        final PropertyCallback classPropertyCallback = configClass.getAnnotation(PropertyCallback.class);

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
                        Class<?> valueClass = getValueClass(field.getGenericType());
                        try {
                            if (valueClass == null) {
                                // look for the return type of the get method?
                                // TODO: better way? scala classes don't extend
                                Method get = type.getMethod("get");
                                valueClass = get.getReturnType();
                            }
                            if (annotation != null) {
                                defaultValue = propBuilder.convert(annotation.value(), valueClass);
                            } else {
                                defaultValue = propBuilder.defaultVal();
                            }
                        } catch (Exception e) {
                            Throwables.propagate(e);
                        }

                        Object property = propBuilder.getProp(propName, defaultValue, valueClass);

                        PropertyCallback propertyCallback = field.getAnnotation(PropertyCallback.class);
                        addCallback(config, property, propertyCallback);
                        addCallback(config, property, classPropertyCallback);

                        field.set(config, property);

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

    @SuppressWarnings("unchecked")
    protected void addCallback(Object config, Object prop, PropertyCallback propertyCallback) {
        if (propertyCallback == null)
            return;
        try {
            Class callbackClass = propertyCallback.value();

            Runnable callback = (Runnable) callbackClass.newInstance();

            if (prop instanceof PropertyWrapper) {
                PropertyWrapper<?> property = (PropertyWrapper<?>) prop;

                if (callback instanceof AbstractCallback) {
                    AbstractCallback abstractCallback = AbstractCallback.class.cast(callback);
                    abstractCallback.setConfig(config);
                    abstractCallback.setProp(property);
                }

                property.addCallback(callback);
            } else {
                System.err.println("prop is not a PropertyWrapper: "+prop.getClass()); //TODO: replace with logging
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    protected String getPropName(Field field, String context) {
        if (StringUtils.isBlank(context))
            return field.getName();

        return context+"."+field.getName();
    }

    @SuppressWarnings("unchecked")
    //TODO: combine with Application.getContextClass
    public static Class<?> getValueClass(Type t) {
        Class<?> originalType;

        if (t instanceof ParameterizedType) {
            originalType = (Class<?>) ((ParameterizedType)t).getRawType();
        } else {
            originalType = (Class<?>) t;
        }
        Class<?> valueClass = null;
        while (t instanceof Class<?>) {
            t = ((Class<?>) t).getGenericSuperclass();
        }
        if (t instanceof ParameterizedType) {
            // should typically have one of type parameters (first one) that matches:
            ParameterizedType parameterizedType = (ParameterizedType) t;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(typeArguments != null && typeArguments.length >= 1, originalType.getName() +
                    " does not have one at least one Value type as type parameter");

            valueClass = (Class<?>)typeArguments[0];
        }

        return valueClass;
    }

}
