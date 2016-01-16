package pl.edu.pw.wsd.agency.agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.message.content.AgentStatistics;

/**
 * Base class for all "real world" agents.
 * @author marcin.czerwinski
 *
 */
public abstract class BaseAgent extends Agent {

    private static final long serialVersionUID = 851946783328690212L;

    private AgentStatistics statistics;

	public BaseAgent() {
    	this.statistics = new AgentStatistics();
	}
    
    public AgentStatistics getAgentStatistics() {
		return statistics;
	}
    
    public ACLMessage receiveAndUpdateStatistics() {
    	ACLMessage message = super.receive();
    	if(message != null) {
    		statistics.incrementMessagesReceived();
    	}
    	return message;
    }
    
    public ACLMessage receiveAndUpdateStatistics(MessageTemplate mt) {
    	ACLMessage message = super.receive(mt);
    	if(message != null) {
    		statistics.incrementMessagesReceived();
    	}
    	return message;
    }
    
    public void sendAndUpdateStatistics(ACLMessage message) {
    	super.send(message);
    	statistics.incrementMessagesSent();
    }
	
}
