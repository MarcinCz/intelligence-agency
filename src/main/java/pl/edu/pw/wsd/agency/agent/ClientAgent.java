package pl.edu.pw.wsd.agency.agent;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientReceiveMessage;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientRequestDeliveryMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientSendMessagesBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.UserInputMessageBehaviour;
import pl.edu.pw.wsd.agency.config.ClientAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Client Agent implementation.
 *
 * @author Adrian Sidor
 * @author Adam Papros
 */
public class ClientAgent extends PhysicalAgent {

	private static final long serialVersionUID = 8776284258546308595L;

	private static final Logger log = LogManager.getLogger();

	private int createStatusPeriod;

	/**
	 * Map that hold messages.
	 * The value of every message is counter -> how many times message was sent.
	 */
	@Getter
	private Map<ClientMessage, Integer> clientMessages;

	@Override
	public Set<MessageId> getStoredMessageId() {
		final Set<MessageId> s = new HashSet<>();
		clientMessages.forEach((clientMessage, integer) -> s.add(clientMessage.getMessageId()));
		return s;
	}

	public ClientAgent(ClientAgentConfiguration config) {
		super(config, true);
		loadConfiguration(config);
	}

	@Override
	protected void setup() {
		super.setup();
		clientMessages = new HashMap<>();

		addBehaviour(new UserInputMessageBehaviour(this));
		addBehaviour(new ClientSendMessagesBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ClientRequestDeliveryMessageBehaviour(this, moveBehaviourPeriod));
		addBehaviour(new ClientReceiveMessage(this, moveBehaviourPeriod));

		addStatusesBehaviours();
	}

	private void addStatusesBehaviours() {
		addBehaviour(new ClientCreateStatusBehaviour(this, createStatusPeriod));
	}

	protected void loadConfiguration(ClientAgentConfiguration config) {
		super.loadConfiguration(config);
		createStatusPeriod = config.getCreateNewStatusPeriod();
	}

	public void queueClientMessage(ClientMessage cm) {
		clientMessages.put(cm, 0);
	}

}
