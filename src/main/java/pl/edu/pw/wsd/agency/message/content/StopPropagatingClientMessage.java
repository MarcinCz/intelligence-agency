package pl.edu.pw.wsd.agency.message.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.MessageId;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopPropagatingClientMessage {

	@JsonProperty("message_id")
	private MessageId messageId;

	public String serialize() {
		try {
			return Configuration.getInstance().getObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}
}
