package halfpipe.groovyexample.resources

import com.google.common.base.Optional
import com.yammer.metrics.annotation.Timed
import halfpipe.groovyexample.ExampleConf
import halfpipe.groovyexample.core.Hello
import org.springframework.stereotype.Component

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 9:28 PM
 */
@Component
@Path("/hello")
class HelloResource {

    @Inject
    ExampleConf config

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Hello hello(@QueryParam("name") Optional<String> name) {
        new Hello(
                hello: config.helloText.get(),
                to: name.or("groovy user"))
    }
}
