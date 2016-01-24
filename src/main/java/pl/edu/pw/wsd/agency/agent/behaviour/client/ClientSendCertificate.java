package pl.edu.pw.wsd.agency.agent.behaviour.client;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class ClientSendCertificate extends OneShotBehaviour {

	private static final long serialVersionUID = 6979789116829708765L;
	private static final Logger log = LogManager.getLogger();

	private ClientAgent agent;

	public ClientSendCertificate(ClientAgent a) {
		super(a);
		this.agent = a;
	}

	@Override
	public void action() {
		Set<PhysicalAgentId> transmitters = agent.getTransmittersInRange();

		for (PhysicalAgentId receiver : transmitters) {
			sendAgentCertificate(receiver);
		}
	}

	private void sendAgentCertificate(PhysicalAgentId receiver) {
		log.debug("Attempting to send certificate to transmitter [" + receiver.getLocalName() + "]");
		ACLMessage aclm = new ACLMessage(ACLMessage.INFORM);
		aclm.addReceiver(receiver.toAID());
		aclm.setLanguage(Language.JSON);
		aclm.setConversationId(ConversationId.DELIVER_AGENT_CERTIFICATES.generateId());
		aclm.setContent(createContent());
		aclm.setSender(agent.getAID());
		agent.sendAndUpdateStatistics(aclm);
		log.debug("Agent statuses from transmitter [" + receiver.getLocalName() + "] requested");
	}

	private String createContent() {
		try {
			AgentCertificate certificate = new AgentCertificate();
			certificate.setAgentId(agent.getName());
			certificate.setPublicKey(agent.getSecurityKeyPair().getPublic());
			certificate.setTimestamp(DateTime.now());
			
			return Configuration.getInstance().getObjectMapper().writeValueAsString(certificate);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not parse agent status to message content", e);
		}
	}
}
