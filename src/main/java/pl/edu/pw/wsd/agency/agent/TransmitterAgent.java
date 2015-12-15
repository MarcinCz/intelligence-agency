package pl.edu.pw.wsd.agency.agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.behaviour.MoveBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.PropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.agent.behaviour.Receive;
import pl.edu.pw.wsd.agency.message.content.PropagateMyMessage;

public class TransmitterAgent extends BaseAgent {

    private static final long serialVersionUID = 4131616609061841238L;
    private static final Logger LOGGER = LogManager.getLogger();
    private List<PropagateMyMessage> propagate = new ArrayList<PropagateMyMessage>();
    

    @Override
	protected void setup() {
		super.setup();
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("transmitter");
		sd.setName("Message Propagation");
		dfd.addServices(sd);
		try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		addBehaviour(new MoveBehaviour(null, 10000, null));
		addBehaviour(new Receive());
		addBehaviour(new PropagateMessageBehaviour());
		
	}
    
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void addPropagateMyMessage(PropagateMyMessage message) {
        this.propagate.add(message);
    }
    
    public List<PropagateMyMessage> getPropagateMyMessageList() {
        return propagate;
    }
    
    
}
