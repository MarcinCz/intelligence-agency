package pl.edu.pw.wsd.agency.agent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientReceiveCertificatesListBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientReceiveMessage;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientRequestCertificatesListBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientSendCertificate;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientSendMessagesBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.UserInputMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.transmitter.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.config.ClientAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

/**
 * Client Agent implementation.
 *
 * @author Adrian Sidor
 * @author Adam Papros
 */
public class ClientAgent extends PhysicalAgent {

	private static final long serialVersionUID = 8776284258546308595L;

	private static final Logger log = LogManager.getLogger();

	private long requestCertificatesPeriod;

	private int createStatusPeriod;

	/**
	 * Map that hold messages sent by client (or messages that will be send).
	 * The value of every message is counter -> how many times message was sent.
	 */
	@Getter
	private final Map<ClientMessage, Integer> clientMessages;

	/**
	 * Map that holds received messages.
	 * The value of every message is counter -> how many times message was delivered.
	 */
	@Getter
	private final Map<ClientMessage, Integer> receivedMessages;

	@Override
	public Set<MessageId> getStoredMessageId() {
		final Set<MessageId> s = new HashSet<>();
		clientMessages.forEach((clientMessage, integer) -> s.add(clientMessage.getMessageId()));
		return s;
	}

	public ClientAgent(ClientAgentConfiguration config) {
		super(config, true);
		loadConfiguration(config);
		this.clientMessages = new HashMap<>();
		this.receivedMessages = new HashMap<>();
	}

	@Override
	protected void setup() {
		super.setup();

		addBehaviour(new RequestAgentsLocationBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ReceiveAgentsLocationBehaviour(this));
		
		addBehaviour(new UserInputMessageBehaviour(this));
		addBehaviour(new ClientSendMessagesBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ClientReceiveMessage(this, moveBehaviourPeriod));
		addCertificatesBehaviours();

		addStatusesBehaviours();
	}

	private void addCertificatesBehaviours() {
		addBehaviour(new ClientSendCertificate(this));
		addBehaviour(new ClientRequestCertificatesListBehaviour(this, requestCertificatesPeriod));
		addBehaviour(new ClientReceiveCertificatesListBehaviour(this));
	}

	private void addStatusesBehaviours() {
		addBehaviour(new ClientCreateStatusBehaviour(this, createStatusPeriod));
	}

	protected void loadConfiguration(ClientAgentConfiguration config) {
		super.loadConfiguration(config);
		createStatusPeriod = config.getCreateNewStatusPeriod();
		requestCertificatesPeriod = config.getRequestCertificatesPeriod();
	}

	public void queueClientMessage(ClientMessage cm) {
		clientMessages.put(cm, 0);
	}

	public void addReceivedMessage(ClientMessage clientMessage) {
		if (receivedMessages.containsKey(clientMessage)) {
			Integer counter = receivedMessages.get(clientMessage);
			// increment
			receivedMessages.put(clientMessage, counter + 1);
		} else {
			receivedMessages.put(clientMessage, 1);
		}
	}

	public void updateCertificates(List<AgentCertificate> AgentCertificates) {
		getAgentCertificates().addAll(AgentCertificates);
	}
}
