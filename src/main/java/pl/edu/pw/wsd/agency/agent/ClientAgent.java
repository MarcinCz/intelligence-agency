package pl.edu.pw.wsd.agency.agent;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientPropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.PhysicalAgentBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.UserInputMessageBehaviour;
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
        Set<MessageId> collect = clientMessages.stream().
                map(ClientMessage::getMessageId).
                collect(Collectors.toSet());
        return collect;
    }

    public ClientAgent(String propertiesFileName) {
        super(propertiesFileName);
    }

    @Override
    protected void setup() {
        super.setup();
        clientMessages = new LinkedList<>();
        addBehaviour(new PhysicalAgentBehaviour(this, moveBehaviourPeriod, false));
        addBehaviour(new UserInputMessageBehaviour());
        addBehaviour(new ClientPropagateMessageBehaviour(this, moveBehaviourPeriod));
        addBehaviour(new ReceiveAgentsLocationBehaviour(this));
        addBehaviour(new RequestAgentsLocationBehaviour(this, moveBehaviourPeriod));

        addStatusesBehaviours();
    }

    private void addStatusesBehaviours() {
        addBehaviour(new ClientCreateStatusBehaviour(this, createStatusPeriod));
    }

    @Override
    protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
        super.loadConfiguration(propertiesFileName);
        ClientAgentConfiguration cfg = configProvider.getClientAgentConfiguration(propertiesFileName);
        createStatusPeriod = cfg.getCreateNewStatusPeriod();
    }

    public void queueClientMessage(ClientMessage cm) {
        clientMessages.add(cm);
    }

    public List<ClientMessage> getClientMessages() {
        return clientMessages;
    }
}
