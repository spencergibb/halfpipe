package halfpipe.configuration.builder;

import com.netflix.config.DynamicBooleanProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:33 PM
*/
@Component
public class BooleanBuilder extends PropBuilder<DynamicBooleanProperty, Boolean> {
    public Class<DynamicBooleanProperty> getPropType() { return DynamicBooleanProperty.class; }
    public Boolean defaultVal() { return false; }

    public DynamicBooleanProperty getProp(String propName, Boolean defaultVal, Class<?> valueClass) {
        return props().getBooleanProperty(propName, defaultVal);
    }
}
