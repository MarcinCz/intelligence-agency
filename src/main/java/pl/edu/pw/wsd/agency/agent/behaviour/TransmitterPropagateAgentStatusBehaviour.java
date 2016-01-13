package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;

/**
 * Propagate stored agent status messages to transmitters in range.
 * @author marcin.czerwinski
 *
 */
public class TransmitterPropagateAgentStatusBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -168645247619168598L;
    private static final Logger log = LogManager.getLogger();

	private TransmitterAgent agent;
	
	public TransmitterPropagateAgentStatusBehaviour(TransmitterAgent a, long period) {
		super(a, period);
		agent = a;
	}

	@Override
	protected void onTick() {
        List<AID> transmitters = agent.getAgentsInRange();

        for(AID receiver: transmitters) {
        	propagateAgentStatuses(receiver);
        }
	}

	private void propagateAgentStatuses(AID receiver) {
		if (receiver != null) {
            List<ACLMessage> messages = agent.getAgentStatusMessages();
            
            //try to send all the statuses
            //if sending of any status fails then end
            for (ACLMessage message : messages) {
            	try {
            		sendMessage(receiver, message);
            	} catch (Exception e) {
            		log.warn("Error while propagating message to receiver [" + receiver + "]"
            				+ "It may have gone out of range. Not attempting to send more messages to this receiver.", e);
            	}
			}
        }
	}

	private void sendMessage(AID receiver, ACLMessage message) {
		if (message != null) {
			log.debug("Attempting to propagate agent status to transmitter [" + receiver + "]");
		    ACLMessage aclm = new ACLMessage(ACLMessage.PROPAGATE);
		    aclm.addReceiver(receiver);
		    aclm.setContent(message.getContent());
		    aclm.setLanguage(message.getLanguage());
		    aclm.setConversationId(message.getConversationId());
		    agent.sendAndUpdateStatistics(aclm);
		    log.debug("Agent status propagated to transmitter [" + receiver + "]");
		}
	}

}
