package pl.edu.pw.wsd.agency.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageId {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("message_id")
    private String messageId;
}
