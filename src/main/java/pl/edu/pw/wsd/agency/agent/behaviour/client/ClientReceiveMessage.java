package pl.edu.pw.wsd.agency.agent.behaviour.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.content.StopPropagatingClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

/**
 * @author apapros
 */
public class ClientReceiveMessage extends TickerBehaviour {

	private static final long serialVersionUID = -4865095272921712993L;

	private static final Logger log = LogManager.getLogger();

	private ClientAgent clientAgent;

	private final ObjectMapper objectMapper = Configuration.getInstance().getObjectMapper();

	public ClientReceiveMessage(ClientAgent clientAgent, long period) {
		super(clientAgent, period);
		this.clientAgent = clientAgent;
	}


	@Override
	public void onTick() {
		MessageTemplate tm = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE),
				MessageTemplate.MatchReceiver(new AID[]{clientAgent.getAID()}));

		// FIXME :: stats ?
		ACLMessage receivedMessage = clientAgent.receiveAndUpdateStatistics(tm);

		if (receivedMessage != null) {
			try {
				ClientMessage clientMessage = objectMapper.readValue(receivedMessage.getContent(), ClientMessage.class);
				String endClient = clientMessage.getEndClient();

				if (!clientAgent.getLocalName().equals(endClient)) {
					throw new IllegalStateException("This message is not for me, agent" + clientAgent + ", message=" + clientMessage);
				}

				// store message
				clientAgent.addReceivedMessage(clientMessage);
				log.info("Received message, {}", clientMessage);


				// send stop propagating message to remove
				ACLMessage reply = receivedMessage.createReply();
				reply.setConversationId(ConversationId.STOP_PROPAGATING_CLIENT_MESSAGE.name());

				StopPropagatingClientMessage stopPropagatingClientMessage = new StopPropagatingClientMessage();
				stopPropagatingClientMessage.setMessageId(clientMessage.getMessageId());
				reply.setContent(stopPropagatingClientMessage.serialize());
				clientAgent.sendAndUpdateStatistics(reply);

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			block();
		}
	}
}