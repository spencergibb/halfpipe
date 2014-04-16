package halfpipe.example.api;

import com.google.common.base.Optional;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private String body;
    private Optional<Date> date = Optional.absent();

    public Message() {}

    public Message(String body) {
        this.body = body;
    }
}
