package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.StopPropagatingClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

import java.io.IOException;

/**
 * Behaviour for receiving all the messages from other agents.
 * Messages are saved in transmitter for later propagation or other actions.
 */
public class TransmitterReceiveMessageBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -4355343485797591490L;

	private static final Logger log = LogManager.getLogger();

	private ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
	private TransmitterAgent transmitterAgent;

	public TransmitterReceiveMessageBehaviour(TransmitterAgent transmitterAgent) {
		super(transmitterAgent);
		this.transmitterAgent = transmitterAgent;
	}

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE),
				MessageTemplate.MatchLanguage(Language.JSON));

		TransmitterAgent agent = transmitterAgent;
		ACLMessage msg = agent.receiveAndUpdateStatistics(template);
		if (msg != null) {
			switch (ConversationId.resolveConversationType(msg.getConversationId())) {
				case PROPAGATE_AGENT_STATUS:
					agent.addAgentStatusMessage(msg);
					log.debug("Transmitter received new agent status.");
					break;
				case PROPAGATE_AGENT_CERTIFICATE:
					agent.addAgentCertificateMessage(msg);
					log.debug("Transmitter received new agent status.");
					break;
				case CLIENT_MESSAGE:
					agent.addNewClientMessage(msg);
					log.debug("Transmitter received new client message.");
					break;
				case STOP_PROPAGATING_CLIENT_MESSAGE:
					try {
						String content = msg.getContent();
						StopPropagatingClientMessage stopPropagatingClientMessage = mapper.readValue(content, StopPropagatingClientMessage.class);

						agent.removeClientMessage(stopPropagatingClientMessage.getMessageId());
						agent.addStopPropagatingClientMessage(stopPropagatingClientMessage.getMessageId());
						log.debug("Transmitter received new client message.");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					throw new IllegalStateException("Unknown conversation type for conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}

	}

}
