package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;
import pl.edu.pw.wsd.agency.message.propagate.MessageToPropagate;

/**
 * Receives request about stored agent statuses from supervisor
 * 
 * @author marcin.czerwinski
 *
 */
public class TransmitterReceiveAgentStatusesRequestBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 6029553786003193249L;
	private static final Logger log = LogManager.getLogger();

	private TransmitterAgent agent;

	public TransmitterReceiveAgentStatusesRequestBehaviour(TransmitterAgent agent) {
		super(agent);
		this.agent = agent;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchLanguage(Language.JSON));

		ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
		if (msg != null) {
			switch (ConversationId.resolveConversationType(msg.getConversationId())) {
			case DELIVER_AGENT_STATUSES:
				log.debug("Transmitter received new request from agent statuses.");
				sendStatusesToSupervisor(msg);
				break;
			default:
				log.debug("Ignoring request with unkown conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}
	}

	private void sendStatusesToSupervisor(ACLMessage msgFromSupervisor) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(createContent());
		msg.setSender(agent.getAID());
		msg.setConversationId(msgFromSupervisor.getConversationId());
		msg.setLanguage(Language.JSON);
		msg.addReceiver(msgFromSupervisor.getSender());
		agent.sendAndUpdateStatistics(msg);
	}
	
	private String createContent() {
		try {
			AgentStatusMessageQueue queue = agent.getAgentStatusQueue();
			List<MessageToPropagate<AgentStatus>> statuses = queue.getQueuedMessages();
			List<AgentStatus> content = new ArrayList<>();
			for (MessageToPropagate<AgentStatus> messageToPropagate : statuses) {
				content.add(messageToPropagate.getContentObject());
			}
			return Configuration.getInstance().getObjectMapper().writeValueAsString(content);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not parse agent status to message content", e);
		}
	}
}
