package thirtytwo.degrees.halfpipe.example.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String home() throws IOException {
        return "forward:/index.html";
    }
}
