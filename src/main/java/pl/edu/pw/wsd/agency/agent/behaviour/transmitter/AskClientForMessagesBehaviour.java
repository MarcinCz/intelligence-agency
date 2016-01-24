package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import java.util.Set;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class AskClientForMessagesBehaviour extends TickerBehaviour {


	private final TransmitterAgent transmitterAgent;

	public AskClientForMessagesBehaviour(TransmitterAgent a, long period) {
		super(a, period);
		this.transmitterAgent = a;
	}

	@Override
	protected void onTick() {

		Set<PhysicalAgentId> clientsInRange = transmitterAgent.getClientsInRange();

		clientsInRange.stream().forEach(physicalAgentId -> {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(physicalAgentId.toAID());
			msg.setConversationId(ConversationId.CLIENT_MESSAGE_REQUEST.name());
			transmitterAgent.sendAndUpdateStatistics(msg);
		});
	}
}
