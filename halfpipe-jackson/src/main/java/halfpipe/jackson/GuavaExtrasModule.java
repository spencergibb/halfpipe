package halfpipe.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.net.HostAndPort;

import java.io.IOException;

// original https://github.com/dropwizard/dropwizard/blob/master/dropwizard-jackson/src/main/java/io/dropwizard/jackson/GuavaExtrasModule.java
public class GuavaExtrasModule extends Module {
    private static class HostAndPortDeserializer extends JsonDeserializer<HostAndPort> {
        @Override
        public HostAndPort deserialize(JsonParser jp,
                                       DeserializationContext ctxt) throws IOException {
            return HostAndPort.fromString(jp.getText());
        }
    }

    private static class CacheBuilderSpecDeserializer extends JsonDeserializer<CacheBuilderSpec> {
        @Override
        public CacheBuilderSpec deserialize(JsonParser jp,
                                            DeserializationContext ctxt) throws IOException {
            final String text = jp.getText();
            if ("off".equalsIgnoreCase(text) || "disabled".equalsIgnoreCase(text)) {
                return CacheBuilderSpec.disableCaching();
            }
            return CacheBuilderSpec.parse(text);
        }
    }

    private static class GuavaExtrasDeserializers extends Deserializers.Base {
        @Override
        public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                        DeserializationConfig config,
                                                        BeanDescription beanDesc) throws JsonMappingException {
            if (CacheBuilderSpec.class.isAssignableFrom(type.getRawClass())) {
                return new CacheBuilderSpecDeserializer();
            }

            if (HostAndPort.class.isAssignableFrom(type.getRawClass())) {
                return new HostAndPortDeserializer();
            }

            return super.findBeanDeserializer(type, config, beanDesc);
        }
    }

    public GuavaExtrasModule() {
    }

    @Override
    public String getModuleName() {
        return "guava-extras";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new GuavaExtrasDeserializers());
    }
}
