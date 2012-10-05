package thirtytwo.degrees.halfpipe.example.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Component;
import thirtytwo.degrees.halfpipe.example.core.Child;
import thirtytwo.degrees.halfpipe.example.core.Hello;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: spencergibb
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
    public Hello hello(@QueryParam("more") Optional<String> more,
                       @QueryParam("name") Optional<String> name) {
        return new Hello(sayer.hello(), "Resource"+more.or(""), new Child(name));
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Timed
    public String receive(@Valid Hello hello) {
        return "received to: "+hello.getTo() +" msg: "+hello.getHello()+
               ": child: "+hello.getChild();
    }
}
