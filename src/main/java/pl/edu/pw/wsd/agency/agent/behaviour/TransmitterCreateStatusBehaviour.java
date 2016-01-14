package pl.edu.pw.wsd.agency.agent.behaviour;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

/**
 * Behaviour which sends new agent status from transmitter.
 * To do that it just add new agent status message to the propagation queue of the sending transmitter.
 * @author marcin.czerwinski
 *
 */
public class TransmitterCreateStatusBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2519821743610253964L;
    private static final Logger log = LogManager.getLogger();

	public TransmitterCreateStatusBehaviour(TransmitterAgent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
        log.info("Attempting to add new agent status for later propagation...");

		ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
		
		TransmitterAgent agent = (TransmitterAgent) myAgent;
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.setSender(agent.getAID());
		msg.setLanguage(Language.JSON);
		msg.setConversationId(ConversationId.PROPAGATE_AGENT_STATUS.generateId());
		try {
			msg.setContent(mapper.writeValueAsString(agent.getAgentStatus()));
		} catch (JsonProcessingException e) {
		    log.error("Agent status could not be mapped to json", e);
		    return;
		}
        agent.addAgentStatusMessage(msg);
        log.info("Added new agent status for later propagation");
	}
	
}
