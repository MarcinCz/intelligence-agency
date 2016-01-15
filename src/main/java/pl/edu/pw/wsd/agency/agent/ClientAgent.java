package pl.edu.pw.wsd.agency.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.ClientPropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.UserInputMessageBehaviour;
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
public class ClientAgent extends MovingAgent {

    private static final long serialVersionUID = 8776284258546308595L;

    private static final Logger log = LogManager.getLogger();

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
        clientMessages = new LinkedList<ClientMessage>();
        addBehaviour(new MoveBehaviour(this, mbp, false));
        addBehaviour(new UserInputMessageBehaviour());
        addBehaviour(new ClientPropagateMessageBehaviour(this, mbp));
        addBehaviour(new ReceiveAgentsLocationBehaviour(this));
        addBehaviour(new RequestAgentsLocationBehaviour(this, mbp));
    }

    public void queueClientMessage(ClientMessage cm) {
        clientMessages.add(cm);
    }

    public List<ClientMessage> getClientMessages() {
        return clientMessages;
    }
}
