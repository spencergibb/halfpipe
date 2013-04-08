package halfpipe.configuration.builder;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
* User: spencergibb
* Date: 4/8/13
* Time: 1:28 PM
*/
@Component
public abstract class PropBuilder<P, T> {
    public abstract Class<P> getPropType();
    public abstract T defaultVal();
    protected ConversionService conversionService;

    /*protected PropBuilder(ConversionService conversionService) {
        this.conversionService = conversionService;
    }*/

    @SuppressWarnings("unchecked")
    public T convert(String s, Class<?> valueClass) throws Exception {
        if (conversionService.canConvert(s.getClass(), valueClass)) {
            return (T) conversionService.convert(s, valueClass);
        }
        throw new IllegalArgumentException("Unable to convert '"+s+" to type "+valueClass);
    }

    public abstract P getProp(String propName, T defaultVal, Class<?> valueClass);

    protected DynamicPropertyFactory props() {
        return DynamicPropertyFactory.getInstance();
    }
}
