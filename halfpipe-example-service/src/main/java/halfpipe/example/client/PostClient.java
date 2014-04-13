package halfpipe.example.client;

import com.netflix.hystrix.HystrixExecutable;
import feign.RequestLine;
import halfpipe.example.model.Post;

import java.util.List;
import java.util.concurrent.Future;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 2:54 PM
 */
public interface PostClient {
    @RequestLine("GET /v1/posts")
    List<Post> posts();

    @RequestLine("GET /v1/posts")
    Future<List<Post>> postsAsync();

    @RequestLine("GET /v1/posts")
    HystrixExecutable<List<Post>> postsExecuatble();
}
