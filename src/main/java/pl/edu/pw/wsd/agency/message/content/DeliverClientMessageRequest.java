package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apapros
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverClientMessageRequest {

    @JsonProperty("client_id")
    private String clientId;
}
