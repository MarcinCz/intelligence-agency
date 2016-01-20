package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.*;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.TransmitterDeliverMessageBehaviour;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class TransmitterAgent extends PhysicalAgent {

	private static final long serialVersionUID = 4131616609061841238L;

	private static final Logger log = LogManager.getLogger();

	private int createStatusPeriod;
	private int propagateStatusPeriod;

	private Map<ACLMessage, Set<TransmitterId>> clientMessages = new HashMap<>();

	private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public Set<MessageId> getStoredMessageId() {
		Set<MessageId> tmpSet = new HashSet<>();
		Set<ACLMessage> aclMessages = clientMessages.keySet();
		for (ACLMessage clientMessage : aclMessages) {
			// FIXME :: OMG
			String content = clientMessage.getContent();
			try {
				ClientMessage clientMessage1 = mapper.readValue(content, ClientMessage.class);
				MessageId messageId = clientMessage1.getMessageId();
				tmpSet.add(messageId);
			} catch (IOException e) {
				log.error("Could not read JSON=" + content, e);
				e.printStackTrace();
			}
		}
		return tmpSet;
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
		clientMessages.put(cm, Sets.newHashSet());
	}

	public void addAgentStatusMessage(ACLMessage msg) {
		agentStatusQueue.queueMessage(msg);
	}

	public AgentStatusMessageQueue getAgentStatusQueue() {
		return agentStatusQueue;
	}
}
