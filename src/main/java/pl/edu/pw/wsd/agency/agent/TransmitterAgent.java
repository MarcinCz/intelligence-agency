package pl.edu.pw.wsd.agency.agent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.AskClientForMessagesBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterDeliverMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterPropagateAgentCertificateBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterPropagateAgentCertificatesListBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterPropagateAgentStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterPropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterReceiveAgentCertificatesListRequestBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterReceiveAgentCertificatesRequestBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterReceiveAgentStatusesRequestBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterReceiveMessageBehaviour;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.propagate.AgentCertificateMessageQueue;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;

@Getter
public class TransmitterAgent extends PhysicalAgent {

	private static final long serialVersionUID = 4131616609061841238L;

	private static final Logger log = LogManager.getLogger();

	private int createStatusPeriod;
	private int propagateStatusPeriod;

	private Map<ClientMessage, Set<PhysicalAgentId>> clientMessages = new HashMap<>();

	private Set<MessageId> stopPropagating = Sets.newHashSet();

	private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();
	
	private AgentCertificateMessageQueue agentCertificateQueue = new AgentCertificateMessageQueue();
	
	@Setter
	private boolean shouldPropagateCertificatesList = false;
	
	private ACLMessage certificatesListMessage;
	
//	private 

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public Set<MessageId> getStoredMessageId() {
		Set<ClientMessage> aclMessages = clientMessages.keySet();
		return aclMessages.stream().map(ClientMessage::getMessageId).collect(Collectors.toSet());

	}

	public TransmitterAgent(TransmitterConfiguration config) {
		super(config, false);
		loadConfiguration(config);
	}

	@Override
	protected void setup() {
		super.setup();

		addBehaviour(new TransmitterReceiveMessageBehaviour(this));
		addBehaviour(new TransmitterPropagateMessageBehaviour(this, moveBehaviourPeriod / 2));
		addBehaviour(new TransmitterDeliverMessageBehaviour(this, moveBehaviourPeriod / 2));

		// ask client for messages
		addBehaviour(new AskClientForMessagesBehaviour(this, moveBehaviourPeriod / 2));

		// request-inform location service behaviours
		addBehaviour(new RequestAgentsLocationBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ReceiveAgentsLocationBehaviour(this));

		addStatusesBehaviours();
		
		addCertificateBehaviours();
	}

	private void addCertificateBehaviours() {
		addBehaviour(new TransmitterPropagateAgentCertificateBehaviour(this, propagateStatusPeriod));
		addBehaviour(new TransmitterReceiveAgentCertificatesRequestBehaviour(this));
		addBehaviour(new TransmitterPropagateAgentCertificatesListBehaviour(this, propagateStatusPeriod));
		addBehaviour(new TransmitterReceiveAgentCertificatesListRequestBehaviour(this));
	}

	private void addStatusesBehaviours() {
		addBehaviour(new TransmitterPropagateAgentStatusBehaviour(this, propagateStatusPeriod));
		addBehaviour(new TransmitterCreateStatusBehaviour(this, createStatusPeriod));
		addBehaviour(new TransmitterReceiveAgentStatusesRequestBehaviour(this));
	}


	protected void loadConfiguration(TransmitterConfiguration config) {
		super.loadConfiguration(config);
		createStatusPeriod = config.getCreateNewStatusPeriod();
		propagateStatusPeriod = config.getPropagateStatusesPeriod();
	}

	public void addNewClientMessage(ACLMessage cm) {
		try {
			ClientMessage clientMessage = mapper.readValue(cm.getContent(), ClientMessage.class);
			if (stopPropagating.contains(clientMessage.getMessageId())) {
				return;
			}
			clientMessages.put(clientMessage, Sets.newHashSet());
		} catch (IOException e) {
			log.error("Could not parse message", e);
		}

	}

	public void addAgentStatusMessage(ACLMessage msg) {
		agentStatusQueue.queueMessage(msg);
	}

	public AgentStatusMessageQueue getAgentStatusQueue() {
		return agentStatusQueue;
	}

	public void removeClientMessage(MessageId messageId) {
		List<ClientMessage> collect = clientMessages.keySet().stream().
				filter(clientMessage -> clientMessage.getMessageId().equals(messageId)).
				collect(Collectors.toList());
		collect.stream().forEach(clientMessage -> clientMessages.remove(clientMessage));

	}

	public void addStopPropagatingClientMessage(MessageId msg) {
		stopPropagating.add(msg);
	}

	public void addAgentCertificateMessage(ACLMessage msg) {
		agentCertificateQueue.queueMessage(msg);
	}

	public void addAgentCertificateListMessage(ACLMessage msg) {
		if(msg.equals(certificatesListMessage)){
			shouldPropagateCertificatesList = false;
		} else{
			shouldPropagateCertificatesList = true;
			certificatesListMessage = msg;
			getAgentCertificates().addAll(readAgentCertificates(msg));
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
