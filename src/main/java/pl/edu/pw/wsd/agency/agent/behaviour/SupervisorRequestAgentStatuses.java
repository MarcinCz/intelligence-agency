package pl.edu.pw.wsd.agency.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

import java.util.Set;

/**
 * Request agent statuses from transmitters in range.
 *
 * @author marcin.czerwinski
 */
public class SupervisorRequestAgentStatuses extends TickerBehaviour {

	private static final long serialVersionUID = 7418981641032427166L;
	private static final Logger log = LogManager.getLogger();

	private SupervisorAgent agent;

	public SupervisorRequestAgentStatuses(SupervisorAgent a, long period) {
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
		log.debug("Attempting to request agent statuses from transmitter [" + receiver.getLocalName() + "]");
		ACLMessage aclm = new ACLMessage(ACLMessage.REQUEST);
		aclm.addReceiver(receiver.toAID());
		aclm.setLanguage(Language.JSON);
		aclm.setConversationId(ConversationId.DELIVER_AGENT_STATUSES.generateId());
		aclm.setSender(agent.getAID());
		agent.sendAndUpdateStatistics(aclm);
		log.debug("Agent statuses from transmitter [" + receiver.getLocalName() + "] requested");
	}

}
