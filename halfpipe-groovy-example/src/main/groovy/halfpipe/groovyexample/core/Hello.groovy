package halfpipe.groovyexample.core

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 9:25 PM
 */
class Hello {
    @NotEmpty
    @JsonProperty
    String hello

    @NotEmpty
    @JsonProperty
    String to
}
