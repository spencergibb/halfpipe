package halfpipe.example.api;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("A nice message")
@Data
public class Message {
    @ApiModelProperty(value = "The message body", required = true)
    private String body;
    @ApiModelProperty("The message date")
    private Optional<Date> date = Optional.absent();

    private Message() {}

    public Message(String body) {
        this.body = body;
    }
}
