package thirtytwo.degrees.halfpipe.example.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Component;
import thirtytwo.degrees.halfpipe.example.core.Hello;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: spencer
 * Date: 9/21/12
 * Time: 5:11 PM
 */
@Component
@Path("/hello")
public class HelloResource {

    @Inject
    Sayer sayer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Hello hello(@QueryParam("more") /*Optional<String>*/String more) {
        return new Hello(sayer.hello(), "Resource"+more/*.or("")*/);
    }
}
