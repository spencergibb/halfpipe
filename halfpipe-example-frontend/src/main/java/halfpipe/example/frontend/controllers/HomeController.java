package halfpipe.example.frontend.controllers;

import com.netflix.hystrix.HystrixExecutable;
import halfpipe.example.client.PostClient;
import halfpipe.example.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import rx.Observable;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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
    public Callable<String> async(final Map<String, Object> model) throws Exception {
        return () -> {
            values(model);
            Future<List<Post>> posts = postClient.postsAsync();
            model.put("posts", posts.get());
            return "home";
        };
    }

    @RequestMapping("/exec")
    public Callable<String> exec(Map<String, Object> model) throws Exception {
        return () -> {
            values(model);
            HystrixExecutable<List<Post>> posts = postClient.postsExecuatble();
            model.put("posts", posts.execute());
            return "home";
        };
    }

    @RequestMapping("/observe")
    public Callable<String> observe(Map<String, Object> model) throws Exception {
        return () -> {
            values(model);
            Observable<List<Post>> posts = postClient.postsObserve();
            model.put("posts", posts.toBlockingObservable().single());
            return "home";
        };
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
