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
import pl.edu.pw.wsd.agency.agent.behaviour.PropagateMessageBehaviour;
import pl.edu.pw.wsd.agency.message.content.PropagateMyMessage;

public class TransmitterAgent extends BaseAgent {

    private static final long serialVersionUID = 4131616609061841238L;

    private static final Logger log = LogManager.getLogger();

    private List<PropagateMyMessage> propagate = new ArrayList<PropagateMyMessage>();

    @Override
    protected void setup() {
        super.setup();
        registerAgent();
        addBehaviour(new MoveBehaviour(null, mbp, true));
        addBehaviour(new DetectAgentsBehaviour(null, mbp));
        // addBehaviour(new Receive());
        addBehaviour(new PropagateMessageBehaviour(this, 1000));

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

    /**
     * Registers Agent in DF Agent's YellowPages.
     */
    private void registerAgent() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agent-movement");
        sd.setName("Position");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            log.error("Could not register agent. Agent terminating");
            doDelete();
        }
    }

    public void addPropagateMyMessage(PropagateMyMessage message) {
        this.propagate.add(message);
    }

    public List<PropagateMyMessage> getPropagateMyMessageList() {
        return propagate;
    }

}
