package halfpipe.example.endpoint;

import halfpipe.example.model.Post;
import halfpipe.example.repo.PostRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: spencergibb
 * Date: 4/10/14
 * Time: 4:33 PM
 */
@Component
@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostsEndpoint {

    @Inject
    PostRepository posts;

    @GET
    public Iterable<Post> get() {
        return posts.findAll();
    }

    @POST
    public Post create(@Valid Post post) {
        Post save = posts.save(post);
        return save;
    }

    @GET
    @Path("{id}")
    public Post get(@PathParam("id") Long id) {
        return posts.findOne(id);
    }

    @PUT
    @Path("{id}")
    public Post update(@Valid Post post, @PathParam("id") Long id) {
        if (posts.exists(id)) {
            return posts.save(post);
        }
        throw new NotFoundException("Post "+id);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        posts.delete(id);
    }


    @GET
    @Path("/test")
    public Post testPost() {
        Post post = new Post();
        post.setAuthor("myauthor");
        post.setBody("mybody");
        post.setId(1L);
        post.setTitle("mytitle");
        return post;
    }
}
