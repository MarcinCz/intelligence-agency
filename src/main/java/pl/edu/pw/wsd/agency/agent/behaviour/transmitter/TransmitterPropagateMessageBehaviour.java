package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransmitterPropagateMessageBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -4865095272921712993L;

    private static final Logger log = LogManager.getLogger();

    private TransmitterAgent transmitterAgent;

    private final static int NUMBER_OF_MESSAGES_AT_ONCE = 5;

    public TransmitterPropagateMessageBehaviour(TransmitterAgent transmitterAgent, long period) {
        super(transmitterAgent, period);
        this.transmitterAgent = transmitterAgent;
    }


    @Override
    public void onTick() {
        TransmitterAgent agent = (TransmitterAgent) myAgent;
        List<TransmitterId> transmitters = agent.getAgentsInRange();
        Map<ACLMessage, Set<TransmitterId>> clientMessages = agent.getClientMessages();

        for (TransmitterId receiver : transmitters) {
            for (Map.Entry<ACLMessage, Set<TransmitterId>> entry : clientMessages.entrySet()) {

                Set<TransmitterId> transmitterIds = entry.getValue();
                ACLMessage aclMessage = entry.getKey();
                // check if message was sent
                if (!transmitterIds.contains(receiver)) {
                    // send message
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                    msg.addReceiver(receiver.toAID());
                    msg.setContent(aclMessage.getContent());
                    msg.setLanguage(aclMessage.getLanguage());
                    msg.setConversationId(aclMessage.getConversationId());

                    // FIXME stats ?
                    transmitterAgent.send(msg);

                    transmitterIds.add(receiver);

                }
            }

        }
    }

}
