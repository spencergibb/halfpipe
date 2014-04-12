package halfpipe.example.frontend.controllers;

import halfpipe.example.client.PostClient;
import halfpipe.example.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 2:42 PM
 */
@Controller
public class HomeController {

    @Inject
    PostClient postClient;

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        values(model);
        List<Post> posts = postClient.posts();
        model.put("posts", posts);
        return "home";
    }

    @RequestMapping("/async")
    public String async(Map<String, Object> model) throws Exception {
        values(model);
        Future<List<Post>> posts = postClient.postsAsync();
        model.put("posts", posts.get());
        return "home";
    }

    private void values(Map<String, Object> model) {
        model.put("message", "Hello World");
        model.put("title", "Hello Home");
        model.put("date", new Date());
    }

    @RequestMapping("/foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }
}
