package halfpipe.configuration.builder;

import halfpipe.configuration.DynaProp;
import halfpipe.configuration.DynamicProp;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:36 PM
*/
@SuppressWarnings("unchecked")
@Component
public class GenericBuilder extends PropBuilder<DynaProp, Object> {
    public Class<DynaProp> getPropType() {
        return DynaProp.class;
    }

    public Object defaultVal() { return null; }

    public DynaProp<Object> getProp(String propName, Object defaultVal, Class<?> valueClass) {
        return new DynamicProp<Object>(propName, defaultVal, valueClass);
    }
}
