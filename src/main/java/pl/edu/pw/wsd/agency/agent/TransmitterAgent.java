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
import pl.edu.pw.wsd.agency.config.TransmitterConfiguration;
import pl.edu.pw.wsd.agency.message.propagate.AgentStatusMessageQueue;

public class TransmitterAgent extends MovingAgent {

	private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private int createStatusPeriod;
    private int propagateStatusPeriod;
    
    private List<ACLMessage> clientMessages = new ArrayList<>();
    private AgentStatusMessageQueue agentStatusQueue = new AgentStatusMessageQueue();
    
    public TransmitterAgent(TransmitterConfiguration config) {
		super(config);
		loadConfiguration(config);
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
		addBehaviour(new TransmitterPropagateAgentStatusBehaviour(this, propagateStatusPeriod));
		addBehaviour(new TransmitterCreateStatusBehaviour(this, createStatusPeriod));
		addBehaviour(new TransmitterReceiveAgentStatusesRequestBehaviour(this));
	}

    protected void loadConfiguration(TransmitterConfiguration config) {
    	super.loadConfiguration(config);
    	createStatusPeriod = config.getCreateNewStatusPeriod();
    	propagateStatusPeriod = config.getPropagateStatusesPeriod();
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
    
    public AgentStatusMessageQueue getAgentStatusQueue() {
    	return agentStatusQueue;
    }
}
