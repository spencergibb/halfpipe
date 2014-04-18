package halfpipe.example.endpoint;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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
@Api(value = "hello", description = "A hello world endpoint")
public class HelloEndpoint {

    @Inject
    private HelloProperties helloProperties;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "message", notes = "a nice hello", response = Message.class)
   	public Message message() {
   		return new Message(helloProperties.getDefaultMessage().get());
   	}
}
