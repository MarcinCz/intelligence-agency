package pl.edu.pw.wsd.agency.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientPropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.UserInputMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientReceiveMessage;
import pl.edu.pw.wsd.agency.agent.behaviour.client.ClientRequestDeliveryMessageBehaviour;
import pl.edu.pw.wsd.agency.config.ClientAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Client Agent implementation.
 *
 * @author Adrian Sidor
 */
public class ClientAgent extends PhysicalAgent {

	private static final long serialVersionUID = 8776284258546308595L;

	private static final Logger log = LogManager.getLogger();

	private int createStatusPeriod;

	private List<ClientMessage> clientMessages;

	@Override
	public Set<MessageId> getStoredMessageId() {
		return clientMessages.stream().
				map(ClientMessage::getMessageId).
				collect(Collectors.toSet());
	}

	public ClientAgent(ClientAgentConfiguration config) {
		super(config, true);
		loadConfiguration(config);
	}

	@Override
	protected void setup() {
		super.setup();
		clientMessages = new LinkedList<>();

		addBehaviour(new UserInputMessageBehaviour(this));
		addBehaviour(new ClientPropagateMessageBehaviour(this, moveBehaviourPeriod));
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
		clientMessages.add(cm);
	}

	public List<ClientMessage> getClientMessages() {
		return clientMessages;
	}
}
