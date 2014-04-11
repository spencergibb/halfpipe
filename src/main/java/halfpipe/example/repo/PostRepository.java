package halfpipe.example.repo;

import halfpipe.example.model.Post;
import org.springframework.data.repository.CrudRepository;

/**
 * User: spencergibb
 * Date: 4/10/14
 * Time: 4:31 PM
 */
public interface PostRepository extends CrudRepository<Post, Long> {
}
