package halfpipe.properties;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import halfpipe.util.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.TypeUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.apache.commons.lang.StringUtils.*;
import static org.springframework.core.annotation.AnnotationUtils.*;
import static org.springframework.util.ReflectionUtils.*;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 2:00 AM
 */
//TODO: extend ConfigurationPropertiesBindingPostProcessor but remove DynaProps?
public class ArchaiusPropertiesProcessor implements BeanPostProcessor {

    @Inject
    @Qualifier("mvcConversionService")
    ConversionService conversionService;

    @Inject
    ArchaiusInitializer initializer;

    /*@Inject
    Validator validator;*/

    @Inject
    BeanUtils beanUtils;

    @PostConstruct
    public void init() {
        initializer.initializeArchaius();;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ArchaiusProperties annotation = findAnnotation(bean.getClass(), ArchaiusProperties.class);
        if (annotation != null) {
            postProcessAfterInitialization(bean, annotation);
        }
        return bean;
    }

    protected void postProcessAfterInitialization(final Object bean, final ArchaiusProperties annotation) {
        doWithFields(bean.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (TypeUtils.isAssignable(field.getType(), DynaProp.class)) {
                    // make the field accessible if defined private
                    ReflectionUtils.makeAccessible(field);
                    Type genericType = field.getGenericType();
                    Type typeArgument = null;
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) genericType;
                        Type[] typeArguments = pType.getActualTypeArguments();
                        if (typeArguments != null && typeArguments.length == 1) {
                            typeArgument = typeArguments[0];
                        }
                    }

                    DynaProp defaultProp = (DynaProp) field.get(bean);
                    Object defaultValue = null;
                    if (defaultProp != null) {
                        defaultValue = defaultProp.getValue();
                        if (typeArgument == null) {
                            typeArgument = defaultValue.getClass();
                        }
                    }

                    String propertyName = field.getName();
                    String prefix = annotation.value();
                    if (isNotBlank(prefix)) {
                        propertyName = prefix + "." + propertyName;
                    }
                    Class<?> valueClass;
                    if (typeArgument instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) typeArgument;
                        valueClass = (Class<?>) pt.getRawType();
                    } else if (typeArgument instanceof Class) {
                        valueClass = (Class<?>) typeArgument;
                    } else {
                        throw new IllegalArgumentException("Unknown valueClass type: "+typeArgument.getClass());
                    }
                    DynamicProp dynamicProp = new DynamicProp(propertyName, defaultValue, valueClass);
                    dynamicProp.conversionService = conversionService;

                    addCallback(bean, dynamicProp, prefix);
                    addCallback(bean, dynamicProp, propertyName);

                    field.set(bean, dynamicProp);
                } else if (!Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalArgumentException(
                            "Field type of "+field.getType()+". Expected field type: " + DynaProp.class.getCanonicalName());
                }
            }
        });
    }

    private void addCallback(Object bean, DynamicProp dynamicProp, String beanPrefix) {
        String beanName = beanPrefix+".callback";
        try {
            Optional<Runnable> callback = beanUtils.getOptionalBean(beanName, Runnable.class);
            if (callback.isPresent()) {
                if (callback.get() instanceof AbstractCallback) {
                    AbstractCallback abstractCallback = AbstractCallback.class.cast(callback.get());
                    abstractCallback.setProperties(bean);
                    abstractCallback.setProp(dynamicProp);
                }
                dynamicProp.addCallback(callback.get());
            } else {
                return;
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }
}