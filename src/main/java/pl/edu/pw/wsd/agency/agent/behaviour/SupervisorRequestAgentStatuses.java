package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

/**
 * Request agent statuses from transmitters in range.
 * @author marcin.czerwinski
 *
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
		List<AID> transmitters = agent.getAgentsInRange();

        for(AID receiver: transmitters) {
        	requestAgentStatuses(receiver);
        }
	}

	private void requestAgentStatuses(AID receiver) {
		log.debug("Attempting to request agent statuses from transmitter [" + receiver.getName() + "]");
	    ACLMessage aclm = new ACLMessage(ACLMessage.REQUEST);
	    aclm.addReceiver(receiver);
	    aclm.setLanguage(Language.JSON);
	    aclm.setConversationId(ConversationId.DELIVER_AGENT_STATUSES.generateId());
	    aclm.setSender(agent.getAID());
	    agent.sendAndUpdateStatistics(aclm);
	    log.debug("Agent statuses from transmitter [" + receiver.getName() + "] requested");
	}

}
