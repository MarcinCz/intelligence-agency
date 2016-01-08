package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;

public class TransmitterPropagateMessageBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -4865095272921712993L;

    private static final Logger log = LogManager.getLogger();

    private static final String LANGUAGE = "JSON";

    private static final int PERFORMATIVE = ACLMessage.PROPAGATE;
    
    private static final String CONVERSATION_ID = "client-message";
    
    public TransmitterPropagateMessageBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    public void onTick() {
        TransmitterAgent agent = (TransmitterAgent) myAgent;
        List<AID> transmitters = agent.getAgentsInRange();
        // we want to propagate message to all possible Transmitters
        // try to send to all at once or one by behaviour cycle ?
        // which behaviour would be closest to reality
        
        // Transmitter needs to remember to which Transmitter it send message
        // or it will send message to the same Transmitter multiple times
        // or mayby we want that behaviour
        
        // I assume for now that can be only one Agent in range
        AID receiver = transmitters.get(0);
        if (receiver != null) {
            List<ACLMessage> messages = agent.getClientMessages();
            // send one message by behaviour cycle or all ?
            // I choosed one by behaviour cycle
            ACLMessage message = messages.remove(0);
            if (message != null) {
                ACLMessage aclm = new ACLMessage(PERFORMATIVE);
                aclm.addReceiver(receiver);
                aclm.setContent(message.getContent());
                aclm.setLanguage(message.getLanguage());
                aclm.setConversationId(message.getConversationId());
                //TODO: propagate message
            }
        }
    }

}
