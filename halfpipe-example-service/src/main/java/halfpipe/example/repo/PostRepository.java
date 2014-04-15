package halfpipe.example.repo;

import halfpipe.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 * User: spencergibb
 * Date: 4/10/14
 * Time: 4:31 PM
 */
//@RepositoryRestResource(collectionResourceRel = "posts", path = "posts")
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    public Post findPostByTitleIgnoreCase(@Param("title") String title);
}
