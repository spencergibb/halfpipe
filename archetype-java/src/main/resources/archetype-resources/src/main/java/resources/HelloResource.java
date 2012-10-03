#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Component;
import ${package}.core.Hello;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/hello")
public class HelloResource {

    @Inject
    Sayer sayer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Hello hello(@QueryParam("more") Optional<String> more) {
        return new Hello(sayer.hello(), "Resource"+more.or(""));
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Timed
    public String receive(@Valid Hello hello) {
        return "received to: "+hello.getTo() +" msg: "+hello.getHello();
    }
}
