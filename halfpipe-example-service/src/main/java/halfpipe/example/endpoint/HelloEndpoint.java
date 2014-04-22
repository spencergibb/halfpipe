package halfpipe.example.endpoint;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import halfpipe.example.api.Message;
import halfpipe.example.properties.HelloProperties;
import halfpipe.logging.Log;
import org.slf4j.Logger;
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

    @Log
    Logger logger;

    @Inject
    private HelloProperties helloProperties;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "message", notes = "a nice hello", response = Message.class)
   	public Message message() {
        logger.info("My Things {}", helloProperties.getMyThings().get());
   		return new Message(helloProperties.getDefaultMessage().get());
   	}
}
