package halfpipe.configuration.builder;

import com.netflix.config.DynamicLongProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:33 PM
*/
@Component
public class LongBuilder extends PropBuilder<DynamicLongProperty, Long> {
    public Class<DynamicLongProperty> getPropType() { return DynamicLongProperty.class; }
    public Long defaultVal() { return Long.MIN_VALUE; }

    public DynamicLongProperty getProp(String propName, Long defaultVal, Class<?> valueClass) {
        return props().getLongProperty(propName, defaultVal);
    }
}
