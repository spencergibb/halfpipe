package halfpipe.configuration.builder;

import com.netflix.config.DynamicIntProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:33 PM
*/
@Component
public class IntBuilder extends PropBuilder<DynamicIntProperty, Integer> {
    public Class<DynamicIntProperty> getPropType() { return DynamicIntProperty.class; }
    public Integer defaultVal() { return Integer.MIN_VALUE; }

    public DynamicIntProperty getProp(String propName, Integer defaultVal, Class<?> valueClass) {
        return props().getIntProperty(propName, defaultVal);
    }
}
