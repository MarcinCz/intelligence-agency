package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

import java.util.List;

/**
 * Every client needs to periodically send it's status to the supervisor.
 * This behaviour creates the status and tries to send it to the transmitters in range.
 * @author marcin.czerwinski
 *
 */
public class ClientCreateStatusBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 3073432197682956697L;

	private ClientAgent agent;
	
    private static final Logger log = LogManager.getLogger();
	
	public ClientCreateStatusBehaviour(ClientAgent a, long period) {
		super(a, period);
		this.agent = a;
	}

	@Override
	protected void onTick() {
        List<TransmitterId> transmitters = agent.getAgentsInRange();

        for(TransmitterId receiver: transmitters) {
        	if (receiver != null) {
    			AgentStatus status = agent.getAgentStatus();
                
            	try {
            		sendStatus(receiver, status);
            	} catch (Exception e) {
            		log.warn("Error while propagating message to receiver [" + receiver.getLocalName() + "]. "
            				+ "It may have gone out of range. Not attempting to send more messages to this receiver.", e);
            	}
            }
        }
	}

	private void sendStatus(TransmitterId receiver, AgentStatus status) {
		log.debug("Attempting to send agent status to transmitter [" + receiver.getLocalName() + "]");
	    ACLMessage aclm = new ACLMessage(ACLMessage.PROPAGATE);
	    aclm.addReceiver(receiver.toAID());
	    aclm.setContent(createContent(status));
	    aclm.setLanguage(Language.JSON);
	    aclm.setConversationId(ConversationId.PROPAGATE_AGENT_STATUS.generateId());
	    aclm.setSender(agent.getAID());
	    agent.sendAndUpdateStatistics(aclm);
	    log.debug("Agent status sent to transmitter [" + receiver.getLocalName() + "]");
	}
	
	private String createContent(AgentStatus status) {
		try {
			return Configuration.getInstance().getObjectMapper().writeValueAsString(status);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not parse agent status to message content", e);
		}
	}

}
