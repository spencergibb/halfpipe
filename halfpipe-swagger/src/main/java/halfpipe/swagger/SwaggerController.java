package halfpipe.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 5:01 PM
 */
@Controller
public class SwaggerController {

    @RequestMapping("/swagger")
    public String swagger(Map<String, Object> model) {
        model.put("apiUrl", "http://localhost:8080/v1/api-docs");
        return "swagger";
    }
}
