package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

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
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;
import pl.edu.pw.wsd.agency.message.envelope.Language;

public class TransmitterReceiveAgentCertificatesListRequestBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 9157690027324862299L;
	private static final Logger log = LogManager.getLogger();

	private TransmitterAgent agent;

	public TransmitterReceiveAgentCertificatesListRequestBehaviour(TransmitterAgent agent) {
		super(agent);
		this.agent = agent;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchLanguage(Language.JSON)
						),
			MessageTemplate.MatchConversationId(ConversationId.DELIVER_AGENTS_CERTIFICATES_LIST.name()));

		ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
		if (msg != null) {
			switch (ConversationId.resolveConversationType(msg.getConversationId())) {
			case DELIVER_AGENTS_CERTIFICATES_LIST:
				log.debug("Transmitter received new request from agent statuses.");
				sendCertificatesListToClient(msg);
				break;
			default:
				log.debug("Ignoring request with unkown conversation id [" + msg.getConversationId() + "]");
			}
		} else {
			block();
		}
	}

	private void sendCertificatesListToClient(ACLMessage msgFromSupervisor) {
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
			List<AgentCertificate> certificates = new ArrayList<>();
			certificates.addAll(agent.getAgentCertificates());
			return Configuration.getInstance().getObjectMapper().writeValueAsString(certificates);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not parse agent status to message content", e);
		}
	}
}
