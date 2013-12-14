package halfpipe.example.api;

import lombok.Data;

@Data
public class Message {
    private String body;

    public Message() {}

    public Message(String body) {
        this.body = body;
    }
}
