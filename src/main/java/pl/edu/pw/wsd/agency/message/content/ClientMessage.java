package pl.edu.pw.wsd.agency.message.content;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientMessage {

    @JsonProperty("end-client") 
    private String endClient;
    @JsonProperty("message") 
    private String message;
    @JsonProperty("message-life") 
    private DateTime messageLife;
    
    @JsonCreator
    public ClientMessage(@JsonProperty("end-client") String endClient,@JsonProperty("message") String message,@JsonProperty("message-life") DateTime messageLife) {
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

    public DateTime getMessageLife() {
        return messageLife;
    }

}
