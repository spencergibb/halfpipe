package halfpipe.configuration.builder;

import com.netflix.config.DynamicStringProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:31 PM
*/
@Component
public class StringPropBuilder extends PropBuilder<DynamicStringProperty, String> {
    public Class<DynamicStringProperty> getPropType() { return DynamicStringProperty.class; }
    public String defaultVal() { return null; }

    public DynamicStringProperty getProp(String propName, String defaultVal, Class<?> valueClass) {
        return props().getStringProperty(propName, defaultVal);
    }
}
