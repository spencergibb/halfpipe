package thirtytwo.degrees.halfpipe.example.core;

import com.google.common.base.Optional;
import com.netflix.config.DynamicStringProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 5:45 PM
 */
public class Child {
    Optional<String> name;

    DynamicStringProperty childconfig;

    public Child(@JsonProperty("name") Optional<String> name,
                 @JsonProperty("childconfig") DynamicStringProperty childconfig ) {
        this.name = name;
        this.childconfig = childconfig;
    }

    public Optional<String> getName() {
        return name;
    }

    public DynamicStringProperty getChildconfig() {
        return childconfig;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("childconfig", childconfig).
                toString();
    }
}
