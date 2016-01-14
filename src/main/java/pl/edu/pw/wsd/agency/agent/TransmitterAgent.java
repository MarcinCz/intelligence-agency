package pl.edu.pw.wsd.agency.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.ReceiveAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.RequestAgentsLocationBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterCreateStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterPropagateAgentStatusBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveAgentStatusesRequestBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.TransmitterReceiveMessageBehaviour;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;
import pl.edu.pw.wsd.agency.message.propagate.MessageToPropagate;

public class TransmitterAgent extends MovingAgent {

	private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private List<ACLMessage> clientMessages = new ArrayList<>();
    private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();
    
    public TransmitterAgent(String propertiesFileName) {
		super(propertiesFileName);
	}
    
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new MoveBehaviour(null, mbp, true));
//        addBehaviour(new DetectAgentsBehaviour(null, mbp));
        addBehaviour(new TransmitterReceiveMessageBehaviour());
        addBehaviour(new ReceiveAgentsLocationBehaviour());
        addBehaviour(new RequestAgentsLocationBehaviour(null, mbp));
        // addBehaviour(new Receive());
//        addBehaviour(new TransmitterPropagateMessageBehaviour(this, 1000));
        
        addStatusesBehaviours();

    }

    private void addStatusesBehaviours() {
		addBehaviour(new TransmitterPropagateAgentStatusBehaviour(this, 1000));
		addBehaviour(new TransmitterCreateStatusBehaviour(this, 2000));
		addBehaviour(new TransmitterReceiveAgentStatusesRequestBehaviour(this));
	}

	public void addClientMessage(ACLMessage cm) {
        clientMessages.add(cm);
    }
    
    public List<ACLMessage> getClientMessages() {
        return clientMessages;
    }

    public void addAgentStatusMessage(ACLMessage msg) {
    	agentStatusQueue.queueMessage(msg);
    }
    
    public List<MessageToPropagate<AgentStatus>> getAgentStatusMessages() {
    	return agentStatusQueue.getQueuedMessages();
    }
}
