package halfpipe.configuration;

import com.netflix.config.PropertyWrapper;

/**
 * User: spencergibb
 * Date: 10/14/12
 * Time: 1:28 PM
 */
public class DynamicProp<V> extends PropertyWrapper<V> {
    protected Class<V> valueClass;

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

        //TODO: another static hack
        if (ConfigurationBuilder.conversions.canConvert(val.getClass(), valueClass)) {
            return ConfigurationBuilder.conversions.convert(val, valueClass);
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
