package pl.edu.pw.wsd.agency.agent.behaviour.client;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class ClientReceiveCertificatesListBehaviour extends CyclicBehaviour {
	
	private static final long serialVersionUID = 8475115522841775696L;
	private static final Logger log = LogManager.getLogger();

	private ClientAgent agent;
	
	public ClientReceiveCertificatesListBehaviour(ClientAgent agent) {
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
			case DELIVER_AGENTS_CERTIFICATES_LIST:
				log.debug("Supvervisor received new response with agent certificates from [" + msg.getSender() + "].");
				agent.updateCertificates(readAgentCertificates(msg));
				break;
			default:
				log.debug("Ignoring request with unkown conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}
	}
	
	private List<AgentCertificate> readAgentCertificates(ACLMessage msg) {
		try {
			return Configuration.getInstance().getObjectMapper().readValue(msg.getContent(), new TypeReference<List<AgentCertificate>>() {});
		} catch (IOException e) {
			throw new IllegalStateException("Could not parse agent statuses from content [" + msg.getContent() + "]");
		}
	}
	
}
