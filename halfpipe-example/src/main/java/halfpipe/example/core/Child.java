package halfpipe.example.core;

import com.google.common.base.Optional;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 5:45 PM
 */
public class Child {
    Optional<String> name;

    public Child(@JsonProperty("name") Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                toString();
    }
}
