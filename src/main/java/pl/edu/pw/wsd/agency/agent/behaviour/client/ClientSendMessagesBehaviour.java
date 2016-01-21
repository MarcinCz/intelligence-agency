package pl.edu.pw.wsd.agency.agent.behaviour.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Behaviour that sends messages.
 * <p>
 * Transmitter starts conversation. Client is requested and just sends response with his messages.
 *
 * @author Adam Papros
 */
public class ClientSendMessagesBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -4865095272921712993L;

	private static final Logger log = LogManager.getLogger();

	private final ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

	private static final String CONVERSATION_ID = ConversationId.CLIENT_MESSAGE_REQUEST.name();

	/**
	 * How many times every message will be sent.
	 */
	private static final int MAX_SEND = 5;

	private ClientAgent clientAgent;

	public ClientSendMessagesBehaviour(ClientAgent clientAgent, long period) {
		super(clientAgent, period);
		this.clientAgent = clientAgent;
	}

	@Override
	public void onTick() {
		// message template
		MessageTemplate t = MessageTemplate.MatchConversationId(CONVERSATION_ID);
		// receive
		ACLMessage msg = clientAgent.receiveAndUpdateStatistics(t);
		if (msg != null) {
			final AID sender = msg.getSender();
			// send filtered messages
			List<ClientMessage> filteredMessages = getFilteredMessages();
			filteredMessages.forEach(clientMessage -> {
				try {
					final String content = mapper.writeValueAsString(clientMessage);
					ACLMessage response = new ACLMessage(ACLMessage.PROPAGATE);
					response.addReceiver(sender);
					response.setContent(content);
					response.setLanguage(Language.JSON);
					response.setConversationId(ConversationId.CLIENT_MESSAGE.generateId());
					clientAgent.sendAndUpdateStatistics(msg);

					log.info("Sent response to transmitter ,{}", sender);
				} catch (JsonProcessingException e) {
					log.error("Could not parse ClientMessage");
				}
			});

			// increment counter
			clientAgent.getClientMessages().entrySet().stream().forEach(e -> e.setValue(e.getValue() + 1));

		} else {
			block();
		}
	}

	public List<ClientMessage> getFilteredMessages() {
		return clientAgent.getClientMessages().entrySet().stream().
				filter(clientMessageIntegerEntry -> clientMessageIntegerEntry.getValue() < MAX_SEND).
				map(Map.Entry::getKey).
				collect(Collectors.<ClientMessage>toList());
	}
}
