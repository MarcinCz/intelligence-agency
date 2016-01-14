package pl.edu.pw.wsd.agency.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.behaviour.*;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * Client Agent implementation.
 *
 * @author Adrian Sidor
 */
public class ClientAgent extends MovingAgent {

    private static final long serialVersionUID = 8776284258546308595L;

    private static final Logger log = LogManager.getLogger();

    private List<ClientMessage> clientMessages;

    public ClientAgent(String propertiesFileName) {
        super(propertiesFileName);
    }

    @Override
    protected void setup() {
        super.setup();
        clientMessages = new LinkedList<ClientMessage>();
        addBehaviour(new MoveBehaviour(this, mbp, false));
        //addBehaviour(new DetectAgentsBehaviour(null, mbp));
        addBehaviour(new UserInputMessageBehaviour());
        addBehaviour(new ClientPropagateMessageBehaviour(this, mbp));
        addBehaviour(new ReceiveAgentsLocationBehaviour(this));
        addBehaviour(new RequestAgentsLocationBehaviour(this, mbp));
/*        addBehaviour(new MoveBehaviour(null, mbp, true));
        addBehaviour(new DetectAgentsBehaviour(null, mbp));
        // addBehaviour(new Receive());
        addBehaviour(new PropagateMessageBehaviour(this, 1000));*/

    }

/*    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    public void queueClientMessage(ClientMessage cm) {
        clientMessages.add(cm);
    }

    public List<ClientMessage> getClientMessages() {
        return clientMessages;
    }
}
