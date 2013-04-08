package halfpipe.configuration.builder;

import com.netflix.config.DynamicFloatProperty;
import halfpipe.configuration.builder.PropBuilder;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:35 PM
*/
@Component
public class FloatBuilder extends PropBuilder<DynamicFloatProperty, Float> {
    public Class<DynamicFloatProperty> getPropType() { return DynamicFloatProperty.class; }
    public Float defaultVal() { return Float.NaN; }

    public DynamicFloatProperty getProp(String propName, Float defaultVal, Class<?> valueClass) {
        return props().getFloatProperty(propName, defaultVal);
    }
}
