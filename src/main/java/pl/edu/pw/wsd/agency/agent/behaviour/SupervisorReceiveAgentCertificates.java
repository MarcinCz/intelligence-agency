package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.SupervisorAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class SupervisorReceiveAgentCertificates extends CyclicBehaviour {
	
	private static final long serialVersionUID = 2932696645378683603L;
	private static final Logger log = LogManager.getLogger();

	private SupervisorAgent agent;
	
	public SupervisorReceiveAgentCertificates(SupervisorAgent agent) {
		super(agent);
		this.agent = agent;
	}
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(
					MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
							MessageTemplate.MatchLanguage(Language.JSON)
							),
				MessageTemplate.MatchConversationId(ConversationId.DELIVER_AGENT_CERTIFICATES.name()));

		ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
		if (msg != null) {
			switch (ConversationId.resolveConversationType(msg.getConversationId())) {
			case DELIVER_AGENT_CERTIFICATES:
				log.debug("Supvervisor received new response with agent certificates from [" + msg.getSender() + "].");
				if (agent.updateCertificates(readAgentCertificates(msg))){
					sendAgentCertificates(msg);
				}
				break;
			default:
				log.debug("Ignoring request with unkown conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}
	}
	
	private void sendAgentCertificates(ACLMessage msg) {
		List<AgentCertificate> certificates= new ArrayList<>();
		certificates.addAll(agent.getAgentCertificates());
		
		ACLMessage aclm = new ACLMessage(ACLMessage.INFORM);
		aclm.setLanguage(Language.JSON);
		aclm.setConversationId(ConversationId.PROPAGATE_AGENTS_CERTIFICATES_LIST.generateId());
		aclm.setSender(agent.getAID());
		aclm.setContent(createContent());
		aclm.addReceiver(msg.getSender());

		agent.sendAndUpdateStatistics(aclm);
				
	}

	private List<AgentCertificate> readAgentCertificates(ACLMessage msg) {
		try {
			return Configuration.getInstance().getObjectMapper().readValue(msg.getContent(), new TypeReference<List<AgentCertificate>>() {});
		} catch (IOException e) {
			throw new IllegalStateException("Could not parse agent statuses from content [" + msg.getContent() + "]");
		}
	}
	
	private String createContent() {
		try {
			List<AgentCertificate> certificates = new ArrayList<>();
			certificates.addAll(agent.getAgentCertificates());
			return Configuration.getInstance().getObjectMapper().writeValueAsString(certificates);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not parse agent status to message content", e);
		}
	}

}
