package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.content.StopPropagatingClientMessage;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class TransmitterPropagateMessageBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -4865095272921712993L;

	private static final Logger log = LogManager.getLogger();

	private TransmitterAgent transmitterAgent;

	public TransmitterPropagateMessageBehaviour(TransmitterAgent transmitterAgent, long period) {
		super(transmitterAgent, period);
		this.transmitterAgent = transmitterAgent;
	}


	@Override
	public void onTick() {

		Set<PhysicalAgentId> transmitters = transmitterAgent.getTransmittersInRange();

		Set<MessageId> stopPropagating = transmitterAgent.getStopPropagating();
		for (PhysicalAgentId transmitter : transmitters) {
			for (MessageId messageId : stopPropagating) {
				ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
				msg.addReceiver(transmitter.toAID());
				msg.setContent(new StopPropagatingClientMessage(messageId).serialize());
				msg.setLanguage(Language.JSON);
				msg.setConversationId(ConversationId.STOP_PROPAGATING_CLIENT_MESSAGE.name());
				transmitterAgent.sendAndUpdateStatistics(msg);
			}

		}


		Map<ClientMessage, Set<PhysicalAgentId>> clientMessages = transmitterAgent.getClientMessages();

		for (PhysicalAgentId receiver : transmitters) {
			for (Map.Entry<ClientMessage, Set<PhysicalAgentId>> entry : clientMessages.entrySet()) {

				Set<PhysicalAgentId> physicalAgentIds = entry.getValue();
				ClientMessage aclMessage = entry.getKey();
				// check if message was sent
				if (!physicalAgentIds.contains(receiver)) {
					// send message
					ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
					msg.addReceiver(receiver.toAID());
					msg.setContent(aclMessage.serialize());
					msg.setLanguage(Language.JSON);
					msg.setConversationId(ConversationId.CLIENT_MESSAGE.name());

					this.transmitterAgent.sendAndUpdateStatistics(msg);

					physicalAgentIds.add(receiver);

				}
			}

		}
	}

}
