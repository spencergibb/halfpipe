package halfpipe.jackson;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.type.JavaType;
import halfpipe.logging.Log;

import java.io.IOException;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 10:16 PM
 */
public class DynamicPropertiesModule extends Module {
    private static final Log LOG = Log.forThisClass();

    @JsonCachable
    private static class DynamicStringPropertyDeserializer extends JsonDeserializer<DynamicStringProperty> {
        @Override
        public DynamicStringProperty deserialize(JsonParser jp,
                                       DeserializationContext ctxt) throws IOException {
            JsonStreamContext parsingContext = jp.getParsingContext();

            StringBuilder path = new StringBuilder();

            while (parsingContext != null) {
                String currentName = parsingContext.getCurrentName();
                if (currentName != null) {
                    if (path.length() > 0)
                        path.insert(0, ".");

                    path.insert(0, currentName);
                }
                parsingContext = parsingContext.getParent();
            }

            LOG.debug("path: {}", path);
            return DynamicPropertyFactory.getInstance().getStringProperty(path.toString(), jp.getText());
            //return HostAndPort.fromString(jp.getText());
        }
    }

    private static class DynamicPropertiesDeserializers extends Deserializers.Base {
        @Override
        public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
                                                        DeserializerProvider provider,
                                                        BeanDescription beanDesc, BeanProperty property)
                                                        throws JsonMappingException
        {
            if (DynamicStringProperty.class.isAssignableFrom(type.getRawClass())) {
                return new DynamicStringPropertyDeserializer();
            }

            return super.findBeanDeserializer(type, config, provider, beanDesc, property);
        }
    }

    @Override
    public String getModuleName() {
        return "dynamic-properties";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new DynamicPropertiesDeserializers());
    }
}
