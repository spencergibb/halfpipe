package halfpipe.jackson;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

// see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/json/AnnotationSensitivePropertyNamingStrategy.java
public class AnnotationSensitivePropertyNamingStrategy extends PropertyNamingStrategy {
    private final PropertyNamingStrategy snakeCase;

    public AnnotationSensitivePropertyNamingStrategy() {
        super();
        this.snakeCase = PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
    }

    @Override
    public String nameForConstructorParameter(MapperConfig<?> config,
                                              AnnotatedParameter ctorParam,
                                              String defaultName) {
        if (ctorParam.getDeclaringClass().isAnnotationPresent(JsonSnakeCase.class)) {
            return snakeCase.nameForConstructorParameter(config, ctorParam, defaultName);
        }
        return super.nameForConstructorParameter(config, ctorParam, defaultName);
    }

    @Override
    public String nameForField(MapperConfig<?> config,
                               AnnotatedField field,
                               String defaultName) {
        if (field.getDeclaringClass().isAnnotationPresent(JsonSnakeCase.class)) {
            return snakeCase.nameForField(config, field, defaultName);
        }

        return super.nameForField(config, field, defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method,
                                      String defaultName) {
        if (method.getDeclaringClass().isAnnotationPresent(JsonSnakeCase.class)) {
            return snakeCase.nameForGetterMethod(config, method, defaultName);
        }
        return super.nameForGetterMethod(config, method, defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method,
                                      String defaultName) {
        if (method.getDeclaringClass().isAnnotationPresent(JsonSnakeCase.class)) {
            return snakeCase.nameForSetterMethod(config, method, defaultName);
        }
        return super.nameForSetterMethod(config, method, defaultName);
    }
}
