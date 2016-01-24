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

public class TransmitterPropagateAgentCertificatesListBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 3739138377984728223L;
	private static final Logger log = LogManager.getLogger();

    private TransmitterAgent agent;

    public TransmitterPropagateAgentCertificatesListBehaviour(TransmitterAgent a, long period) {
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
        	if (agent.isShouldPropagateCertificatesList()){
        		ACLMessage message = agent.getCertificatesListMessage();

                try {
                    //dont propagate message to message sender or to itself
                    if (receiver.getLocalName().equals(message.getSender().getLocalName())
                            || receiver.getLocalName().equals(agent.getLocalName())) {
                        return;
                    }

                    propagateMessage(receiver, message);
                    agent.setShouldPropagateCertificatesList(false);
                } catch (Exception e) {
                    log.warn("Error while propagating message to receiver [" + receiver.getLocalName() + "]"
                            + "It may have gone out of range. Not attempting to send more messages to this receiver.", e);
                }
        	}
        }
    }

    private void propagateMessage(AID receiver, ACLMessage message) {
        if (message != null) {
            log.debug("Attempting to propagate agent certificate list to transmitter [" + receiver.getLocalName() + "]");
            ACLMessage aclm = new ACLMessage(ACLMessage.PROPAGATE);
            aclm.addReceiver(receiver);
            aclm.setContent(message.getContent());
            aclm.setLanguage(message.getLanguage());
            aclm.setConversationId(message.getConversationId());
            aclm.setSender(agent.getAID());
            agent.sendAndUpdateStatistics(aclm);
            log.debug("Agent certificates list propagated to transmitter [" + receiver.getLocalName() + "]");
        }
    }

}
