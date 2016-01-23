package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.*;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TransmitterAgent extends PhysicalAgent {

	private static final long serialVersionUID = 4131616609061841238L;

	private static final Logger log = LogManager.getLogger();

	private int createStatusPeriod;
	private int propagateStatusPeriod;

	private Map<ClientMessage, Set<PhysicalAgentId>> clientMessages = new HashMap<>();

	private Set<MessageId> stopPropagating = Sets.newHashSet();

	private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();

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
}
