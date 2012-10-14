package thirtytwo.degrees.halfpipe.configuration;

import static org.springframework.util.ReflectionUtils.findMethod;

import com.netflix.config.DynamicProperty;
import com.netflix.config.PropertyWrapper;
import org.springframework.util.Assert;

import java.lang.reflect.*;

/**
 * User: spencergibb
 * Date: 10/14/12
 * Time: 1:28 PM
 */
public class DynamicProp<V> {
    protected DynamicProperty prop;
    protected V defaultValue;

    protected Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public DynamicProp(String propName, V defaultValue, Class<?> valueClass) {
        this.prop = DynamicProperty.getInstance(propName);
        this.defaultValue = defaultValue;
        this.valueClass = (Class<V>) valueClass;
        if (this.valueClass == null)
            throw new IllegalStateException("Can not figure out value type parameterization for "+getClass().getName());
    }

    @SuppressWarnings("unchecked")
    //TODO: combine with Application.getContextClasses
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


    public V get() {
        String val = prop.getString();
        if (val == null) {
            return defaultValue;
        }

        return convert(valueClass, val);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Class<T> valueClass, String s) {
        try {
            Constructor<T> c = valueClass.getConstructor(String.class);
            T o = c.newInstance(s);
            return o;
        } catch (Exception e) {
            //e.printStackTrace();
            //ignore
        }

        Method valueOf = findMethod(valueClass, "valueOf", String.class);
        try {
            Object o = valueOf.invoke(null, s);
            return (T)o;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public V getValue() { return get(); }

    public String getName() {
        return prop.getName();
    }

    /**
     * Called when the property value is updated.
     * The default does nothing.
     * Subclasses are free to override this if desired.
     */
    protected void propertyChanged() {
        // by default, do nothing
    }

    /**
     * Gets the time (in milliseconds past the epoch) when the property
     * was last set/changed.
     */
    public long getChangedTimestamp() {
        return prop.getChangedTimestamp();
    }

    /**
     * Add the callback to be triggered when the value of the property is changed
     *
     * @param callback
     */
    public void addCallback(Runnable callback) {
        prop.addCallback(callback);
    }

    @Override
    public String toString() {
        String def;

        if (defaultValue == null) {
            def = null;
        } else {
            def = defaultValue.toString();
        }

        return "DynamicProp: {name=" + prop.getName() + ", current value="
                + prop.getString() + ", default = "+def+"}";
    }

}
