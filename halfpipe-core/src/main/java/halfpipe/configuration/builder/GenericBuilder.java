package halfpipe.configuration.builder;

import halfpipe.configuration.DynamicProp;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:36 PM
*/
@SuppressWarnings("unchecked")
@Component
public class GenericBuilder extends PropBuilder<DynamicProp, Object> {
    public Class<DynamicProp> getPropType() {
        return DynamicProp.class;
    }

    public Object defaultVal() { return null; }

    public DynamicProp<Object> getProp(String propName, Object defaultVal, Class<?> valueClass) {
        return new DynamicProp<Object>(propName, defaultVal, valueClass);
    }
}
