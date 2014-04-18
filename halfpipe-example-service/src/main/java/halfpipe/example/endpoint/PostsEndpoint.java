package halfpipe.example.endpoint;

import halfpipe.example.model.Post;
import halfpipe.example.repo.PostRepository;
import org.springframework.data.domain.PageRequest;
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
    public Iterable<Post> get(@QueryParam("page") @DefaultValue("0") int page) {
        return posts.findAll(new PageRequest(page, 2));
    }

    /*@GET
    public Iterable<Post> get(@QueryParam("page") Optional<Integer> page) {
        return posts.findAll(new PageRequest(page.or(0), 2));
    }*/

    @POST
    public Post create(@Valid Post post) {
        Post save = posts.save(post);
        return save;
    }

    @GET
    @Path("{id}")
    public Post get(@PathParam("id") Long id) {
        return posts.getOne(id);
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
    @Path("findbytitle/{title}")
    public Post findByTitle(@PathParam("title") String title) {
        return posts.findPostByTitleIgnoreCase(title);
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
