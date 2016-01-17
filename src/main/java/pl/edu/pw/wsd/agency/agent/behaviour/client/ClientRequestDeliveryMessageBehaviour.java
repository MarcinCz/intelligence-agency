package pl.edu.pw.wsd.agency.agent.behaviour.client;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

import java.util.List;

/**
 * @author apapros
 */
// FIXME must be removed
public class ClientRequestDeliveryMessageBehaviour extends TickerBehaviour {


    private static final Logger log = LogManager.getLogger();

    private ClientAgent clientAgent;

    public ClientRequestDeliveryMessageBehaviour(ClientAgent clientAgent, long period) {
        super(clientAgent, period);
        this.clientAgent = clientAgent;
    }


    @Override
    protected void onTick() {
        List<TransmitterId> transmitters = clientAgent.getAgentsInRange();
        for (TransmitterId transmitterId : transmitters) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(transmitterId.toAID());
            msg.setConversationId(ConversationId.DELIVER_CLIENT_MESSAGE.toString());
            clientAgent.sendAndUpdateStatistics(msg);
        }

    }
}
