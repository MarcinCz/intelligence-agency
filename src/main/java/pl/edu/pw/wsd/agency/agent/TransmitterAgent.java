package pl.edu.pw.wsd.agency.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveMessageBehaviour;

public class TransmitterAgent extends MovingAgent {

	private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private List<ACLMessage> clientMessages = new ArrayList<>();
    private List<ACLMessage> agentStatusMessages = new ArrayList<>();

    public TransmitterAgent(String propertiesFileName) {
		super(propertiesFileName);
	}
    
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new MoveBehaviour(null, mbp, true));
        //addBehaviour(new DetectAgentsBehaviour(null, mbp));
        addBehaviour(new TransmitterReceiveMessageBehaviour());
        addBehaviour(new ReceiveAgentsLocationBehaviour());
        addBehaviour(new RequestAgentsLocationBehaviour(null, mbp));
        // addBehaviour(new Receive());
        //addBehaviour(new PropagateMessageBehaviour(this, 1000));

    }

    public void addClientMessage(ACLMessage cm) {
        clientMessages.add(cm);
    }
    
    public List<ACLMessage> getClientMessages() {
        return clientMessages;
    }

    public void addAgentStatusMessage(ACLMessage msg) {
    	agentStatusMessages.add(msg);
    }
    
    public List<ACLMessage> getAgentStatusMessages() {
    	return agentStatusMessages;
    }
}
