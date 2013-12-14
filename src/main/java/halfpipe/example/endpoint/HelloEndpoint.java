package halfpipe.example.endpoint;

import halfpipe.example.config.HelloProperties;
import halfpipe.example.api.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/hello")
@XmlRootElement
public class HelloEndpoint {

    @Inject
    private HelloProperties helloProperties;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
   	public Message message() {
   		return new Message(helloProperties.getDefaultMessage());
   	}
}
