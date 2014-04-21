package halfpipe.example.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * User: spencergibb
 * Date: 4/10/14
 * Time: 4:29 PM
 */
@Entity
@Data
@ApiModel("A blog post")
public class Post implements Serializable {
    @Id
    @GeneratedValue
    @ApiModelProperty(value = "The post id")
    private Long id;

    @Column(nullable = false)
    @ApiModelProperty(value = "The post title", required = true)
    private String title;

    @Column(nullable = false)
    @ApiModelProperty(value = "The post body", required = true)
    private String body;

    @Column(nullable = false)
    @ApiModelProperty(value = "The post author", required = true)
    private String author;

}
