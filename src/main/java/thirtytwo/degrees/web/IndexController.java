package thirtytwo.degrees.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String home() throws IOException {
        return "forward:/index.html";
    }
}
