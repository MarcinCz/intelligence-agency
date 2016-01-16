package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

/**
 * Receive agent statuses from transmitters and store them.
 * @author marcin.czerwinski
 *
 */
public class SupervisorReceiveAgentStatuses extends CyclicBehaviour {
	
	private static final long serialVersionUID = 182126169795411008L;
    private static final Logger log = LogManager.getLogger();

	private SupervisorAgent agent;
	
	public SupervisorReceiveAgentStatuses(SupervisorAgent agent) {
		super(agent);
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchLanguage(Language.JSON));

		ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
		if (msg != null) {
			switch (ConversationId.resolveConversationType(msg.getConversationId())) {
			case DELIVER_AGENT_STATUSES:
				log.debug("Supvervisor received new response with agent statuses from [" + msg.getSender() + "].");
				agent.updateStatuses(readAgentStatuses(msg));
				break;
			default:
				log.debug("Ignoring request with unkown conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}
	}
	
	private List<AgentStatus> readAgentStatuses(ACLMessage msg) {
		try {
			return Configuration.getInstance().getObjectMapper().readValue(msg.getContent(), new TypeReference<List<AgentStatus>>() {});
		} catch (IOException e) {
			throw new IllegalStateException("Could not parse agent statuses from content [" + msg.getContent() + "]");
		}
	}

}
