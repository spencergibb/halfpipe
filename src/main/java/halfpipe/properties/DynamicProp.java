package halfpipe.properties;

import com.netflix.config.PropertyWrapper;
import org.springframework.core.convert.ConversionService;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 5:26 PM
 */
public class DynamicProp<V> extends PropertyWrapper<V> implements DynaProp<V> {
    protected ConversionService conversionService;
    protected Class<V> valueClass;

    public static final String DUMMY_PROP_NAME = "____dummy__****__prop____";

    //TODO: support collection properties

    public static <V> DynaProp<V> prop(V val) {
        return new DynamicProp<>(DUMMY_PROP_NAME, val);
    }

    public DynamicProp(String propName, V defaultValue) {
        this(propName, defaultValue, defaultValue.getClass());
    }

    @SuppressWarnings("unchecked")
    public DynamicProp(String propName, V defaultValue, Class<?> valueClass) {
        super(propName, defaultValue);
        if (valueClass == null)
            throw new IllegalStateException("Can not figure out value type parameterization for "+getClass().getName());
        this.valueClass = (Class<V>) valueClass;
    }

    public V get() {
        String val = prop.getString();
        if (val == null) {
            return defaultValue;
        }

        if (conversionService != null && conversionService.canConvert(val.getClass(), valueClass)) {
            return conversionService.convert(val, valueClass);
        }
        throw new IllegalArgumentException("Unable to convert '"+val+" to type "+valueClass);
    }

    public V getValue() { return get(); }

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
