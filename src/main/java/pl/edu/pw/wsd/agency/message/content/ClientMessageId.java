package pl.edu.pw.wsd.agency.message.content;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
@Data
@NoArgsConstructor
public class ClientMessageId {

	private String clientId;

	private String messageId;


}
