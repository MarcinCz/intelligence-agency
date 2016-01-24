package pl.edu.pw.wsd.agency.agent.behaviour.client;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class ClientRequestCertificatesListBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -1756720122192821060L;
	private static final Logger log = LogManager.getLogger();

	private ClientAgent agent;

	public ClientRequestCertificatesListBehaviour(ClientAgent a, long period) {
		super(a, period);
		this.agent = a;
	}

	@Override
	protected void onTick() {
		Set<PhysicalAgentId> transmitters = agent.getTransmittersInRange();

		for (PhysicalAgentId receiver : transmitters) {
			requestAgentStatuses(receiver);
		}
	}

	private void requestAgentStatuses(PhysicalAgentId receiver) {
		log.debug("Attempting to request agent certificates from transmitter [" + receiver.getLocalName() + "]");
		ACLMessage aclm = new ACLMessage(ACLMessage.REQUEST);
		aclm.addReceiver(receiver.toAID());
		aclm.setLanguage(Language.JSON);
		aclm.setConversationId(ConversationId.DELIVER_AGENTS_CERTIFICATES_LIST.generateId());
		aclm.setSender(agent.getAID());
		agent.sendAndUpdateStatistics(aclm);
		log.debug("Agent certificates list from transmitter [" + receiver.getLocalName() + "] requested");
	}

}
