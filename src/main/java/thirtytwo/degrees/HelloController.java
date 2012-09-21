package thirtytwo.degrees;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello")
    public void home(Writer out) throws IOException {
        System.out.println("HomeController: Passing through...");
        out.write("hello heroku");
        out.flush();
        out.close();
    }
}
