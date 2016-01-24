package pl.edu.pw.wsd.agency.agent.behaviour.transmitter;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.message.content.AgentCertificate;
import pl.edu.pw.wsd.agency.message.propagate.AgentCertificateMessageQueue;
import pl.edu.pw.wsd.agency.message.propagate.MessageToPropagate;

public class TransmitterPropagateAgentCertificateBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -168645247619168598L;
    private static final Logger log = LogManager.getLogger();

    private TransmitterAgent agent;

    public TransmitterPropagateAgentCertificateBehaviour(TransmitterAgent a, long period) {
        super(a, period);
        agent = a;
    }

    @Override
    protected void onTick() {
        Set<PhysicalAgentId> transmitters = agent.getTransmittersInRange();

        for (PhysicalAgentId receiver : transmitters) {
            propagateAgentStatuses(receiver.toAID());
        }
    }

    private void propagateAgentStatuses(AID receiver) {
        if (receiver != null) {
            AgentCertificateMessageQueue queue = agent.getAgentCertificateQueue();
            List<MessageToPropagate<AgentCertificate>> messages = queue.getQueuedMessages();

            //try to send all the statuses
            //if sending of any status fails then end
            for (MessageToPropagate<AgentCertificate> message : messages) {
                try {
                    //dont propagate message to message sender or to itself
                    if (receiver.getLocalName().equals(message.getACLMessage().getSender().getLocalName())
                            || receiver.getLocalName().equals(agent.getLocalName())) {
                        continue;
                    }

                    propagateMessage(receiver, message.getACLMessage());
                    queue.remove(message); //in real world it probably should be stored longer
                } catch (Exception e) {
                    log.warn("Error while propagating message to receiver [" + receiver.getLocalName() + "]"
                            + "It may have gone out of range. Not attempting to send more messages to this receiver.", e);
                }
            }
        }
    }

    private void propagateMessage(AID receiver, ACLMessage message) {
        if (message != null) {
            log.debug("Attempting to propagate agent certificate to transmitter [" + receiver.getLocalName() + "]");
            ACLMessage aclm = new ACLMessage(ACLMessage.PROPAGATE);
            aclm.addReceiver(receiver);
            aclm.setContent(message.getContent());
            aclm.setLanguage(message.getLanguage());
            aclm.setConversationId(message.getConversationId());
            aclm.setSender(agent.getAID());
            agent.sendAndUpdateStatistics(aclm);
            log.debug("Agent certificate propagated to transmitter [" + receiver.getLocalName() + "]");
        }
    }

}
