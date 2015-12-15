package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PropagateMyMessage {

    @JsonProperty("end-client") 
    private String endClient;
    @JsonProperty("message") 
    private String message;
    @JsonProperty("message-life") 
    private String messageLife;
    
    @JsonCreator
    public PropagateMyMessage(@JsonProperty("end-client") String endClient,@JsonProperty("message") String message,@JsonProperty("message-life") String messageLife) {
        this.endClient = endClient;
        this.message = message;
        this.messageLife = messageLife;
    }

    public String getEndClient() {
        return endClient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageLife() {
        return messageLife;
    }

}
