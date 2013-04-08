package halfpipe.configuration.builder;

import com.netflix.config.DynamicDoubleProperty;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:35 PM
*/
@Component
public class DoubleBuilder extends PropBuilder<DynamicDoubleProperty, Double> {
    public Class<DynamicDoubleProperty> getPropType() { return DynamicDoubleProperty.class; }
    public Double defaultVal() { return Double.NaN; }

    public DynamicDoubleProperty getProp(String propName, Double defaultVal, Class<?> valueClass) {
        return props().getDoubleProperty(propName, defaultVal);
    }
}
