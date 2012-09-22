package thirtytwo.degrees.halfpipe.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello")
    public void home(Writer out) throws IOException {
        System.out.println("HelloController: Passing through...");
        out.write("hello controller");
        out.flush();
        out.close();
    }
}
