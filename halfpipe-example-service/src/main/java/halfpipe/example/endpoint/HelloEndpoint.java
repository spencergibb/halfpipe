package halfpipe.example.endpoint;

import halfpipe.example.api.Message;
import halfpipe.example.properties.HelloProperties;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/hello")
public class HelloEndpoint {

    @Inject
    private HelloProperties helloProperties;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
   	public Message message() {
   		return new Message(helloProperties.getDefaultMessage().get());
   	}
}
