package halfpipe.configuration.builder;

import com.netflix.config.DynamicStringMapProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:33 PM
*/
@Component
public class StringMapBuilder extends PropBuilder<DynamicStringMapProperty, String> {
    public Class<DynamicStringMapProperty> getPropType() { return DynamicStringMapProperty.class; }
    public String defaultVal() { return null; }

    public DynamicStringMapProperty getProp(String propName, String defaultVal, Class<?> valueClass) {
        return new DynamicStringMapProperty(propName, defaultVal);
    }
}
