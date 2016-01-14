package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
public class ClientMessage {

    @JsonProperty("end-client")
    private String endClient;
    @JsonProperty("message")
    private String message;
    @JsonProperty("message-life")
    private DateTime messageLife;

    @JsonCreator
    public ClientMessage(@JsonProperty("end-client") String endClient, @JsonProperty("message") String message, @JsonProperty("message-life") DateTime messageLife) {
        this.endClient = endClient;
        this.message = message;
        this.messageLife = messageLife;
    }

}
