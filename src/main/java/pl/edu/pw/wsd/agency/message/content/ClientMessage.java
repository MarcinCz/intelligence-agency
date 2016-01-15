package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.edu.pw.wsd.agency.location.MessageId;

@Getter
public class ClientMessage {

    @JsonProperty("message_id")
    private MessageId messageId;
    @JsonProperty("end-client")
    private String endClient;
    @JsonProperty("message")
    private String message;
    @JsonProperty("message-life")
    private long messageLife;

    @JsonCreator
    public ClientMessage(@JsonProperty("message_id") MessageId messageId,
                         @JsonProperty("end-client") String endClient,
                         @JsonProperty("message") String message,
                         @JsonProperty("message-life") long messageLife) {
        this.messageId = messageId;
        this.endClient = endClient;
        this.message = message;
        this.messageLife = messageLife;
    }

}
