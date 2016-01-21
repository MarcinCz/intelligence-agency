package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;

import java.util.Map;
import java.util.Set;

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
		Map<ACLMessage, Set<PhysicalAgentId>> clientMessages = transmitterAgent.getClientMessages();

		for (PhysicalAgentId receiver : transmitters) {
			for (Map.Entry<ACLMessage, Set<PhysicalAgentId>> entry : clientMessages.entrySet()) {

				Set<PhysicalAgentId> physicalAgentIds = entry.getValue();
				ACLMessage aclMessage = entry.getKey();
				// check if message was sent
				if (!physicalAgentIds.contains(receiver)) {
					// send message
					ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
					msg.addReceiver(receiver.toAID());
					msg.setContent(aclMessage.getContent());
					msg.setLanguage(aclMessage.getLanguage());
					msg.setConversationId(aclMessage.getConversationId());

					// FIXME stats ?
					this.transmitterAgent.send(msg);

					physicalAgentIds.add(receiver);

				}
			}

		}
	}

}
