package halfpipe.mgmt.resources;

import com.google.common.base.Optional;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: spencergibb
 * Date: 9/25/12
 * Time: 6:13 AM
 */
@Component
@Path("/mgmt/gc")
public class GCResource {

    @POST //TODO: force post in security?
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.WILDCARD)
    public Response execute(@QueryParam("num") Optional<Integer> num) {
        final int count = num.or(1);

        StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                for (int i=0; i < count; i++) {
                    out.write("GC Running\n".getBytes());
                    Runtime.getRuntime().gc();
                }
            }
        };
        return Response.ok(output, MediaType.TEXT_PLAIN_TYPE).build();
    }
}
