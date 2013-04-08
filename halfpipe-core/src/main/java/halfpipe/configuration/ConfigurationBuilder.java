package halfpipe.configuration;

import static org.springframework.util.ReflectionUtils.*;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.netflix.config.*;
import halfpipe.configuration.builder.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:19 PM
 * json config http://svn.apache.org/viewvc/commons/proper/configuration/branches/configuration2_experimental/src/main/java/org/apache/commons/configuration2/
 */
public class ConfigurationBuilder {
    public static ConversionService conversions;

    protected List<PropBuilder<?, ?>> builders = Lists.newArrayList();

    ConversionService conversionService;

    public ConfigurationBuilder(ConversionService conversionService, List<PropBuilder<?, ?>> builders) {
        this.builders.addAll(builders);
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

        doWithFields(configClass, new FieldCallback() {
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        makeAccessible(field);
                        Class<?> type = field.getType();
                        String propName = getPropName(field, context);

                        boolean fieldSet = false;
                        for (PropBuilder propBuilder : builders) {
                            if (propBuilder.getPropType().isAssignableFrom(type)) {
                                Object defaultValue = null;
                                //TODO: use spring converters
                                Class<?> valueClass = getValueClass(field.getGenericType());
                                try {
                                    if (valueClass == null) {
                                        // look for the return type of the get method?
                                        // TODO: better way? scala classes don't extend
                                        Method get = type.getMethod("get");
                                        valueClass = get.getReturnType();
                                    }
                                    defaultValue = propBuilder.defaultVal();

                                    Object value = getDefaultValue(field.get(config));
                                    if (value != null)
                                        defaultValue = value;
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
                    }
                }, new FieldFilter() {
                    @Override
                    public boolean matches(Field field) {
                        String name = field.getName();
                        // for groovy
                        return !name.startsWith("$") && !name.equals("metaClass");
                    }
                }
        );
    }

    protected Object getDefaultValue(Object fieldValue) {
        if (fieldValue != null && fieldValue instanceof PropertyWrapper) {
                PropertyWrapper pw = (PropertyWrapper) fieldValue;
                Object value = pw.getValue();
                if (value != null)
                    return value;
        }
        return null;
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
