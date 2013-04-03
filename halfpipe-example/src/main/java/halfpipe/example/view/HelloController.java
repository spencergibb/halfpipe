package halfpipe.example.view;

import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import halfpipe.logging.Log;

import java.io.IOException;
import java.io.Writer;

@Controller
public class HelloController {
    private static final Log LOG = Log.forThisClass();

    @RequestMapping("/hello")
    @Timed
    public void home(Writer out) throws IOException {
        LOG.warn("HelloController: Passing through...");
        out.write("hello controller");
        out.flush();
        out.close();
    }
}
