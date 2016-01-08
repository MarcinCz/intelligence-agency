package pl.edu.pw.wsd.agency.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import pl.edu.pw.wsd.agency.agent.behaviour.DetectAgentsBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveClientMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

public class TransmitterAgent extends MovingAgent {

    private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private List<ClientMessage> clientMessages = new ArrayList<ClientMessage>();

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new MoveBehaviour(null, mbp, true));
        //addBehaviour(new DetectAgentsBehaviour(null, mbp));
        addBehaviour(new ReceiveClientMessageBehaviour());
        addBehaviour(new ReceiveAgentsLocationBehaviour());
        addBehaviour(new RequestAgentsLocationBehaviour(null, mbp));
        // addBehaviour(new Receive());
        //addBehaviour(new PropagateMessageBehaviour(this, 1000));

    }

    public void addClientMessage(ClientMessage cm) {
        clientMessages.add(cm);
    }
    
    public List<ClientMessage> getClientMessages() {
        return clientMessages;
    }

}
