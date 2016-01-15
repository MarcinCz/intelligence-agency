package pl.edu.pw.wsd.agency.agent.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.MovingAgent;
import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.message.envelope.ConversationId;

/**
 * Behaviour for receiving all the messages from other agents.
 * Messages are saved in transmitter for later propagation or other actions.
 */
public class TransmitterReceiveMessageBehaviour extends Behaviour {

    private static final long serialVersionUID = -4355343485797591490L;

    private static final Logger log = LogManager.getLogger();

    private static final String LANGUAGE = "JSON";

    private static final int PERFORMATIVE = ACLMessage.PROPAGATE;

    private MovingAgent movingAgent;

    public TransmitterReceiveMessageBehaviour(MovingAgent movingAgent) {
        super(movingAgent);
        this.movingAgent = movingAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt2 = MessageTemplate.and(
                MessageTemplate.MatchPerformative(PERFORMATIVE),
                MessageTemplate.MatchLanguage(LANGUAGE));
        /*MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId(CONVERSATION_ID), 
                mt2);*/
        TransmitterAgent agent = (TransmitterAgent) myAgent;
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt2);
        if (msg != null) {
            switch (ConversationId.resolveConversationType(msg.getConversationId())) {
                case AGENT_STATUS:
                    agent.addAgentStatusMessage(msg);
                    log.debug("Transmitter received new agent status.");
                    break;
                case CLIENT_MESSAGE:
                    agent.addClientMessage(msg);
                    log.debug("Transmitter received new client message.");
                    break;
                default:
                    throw new IllegalStateException("Unknown conversation type for conversation id [" + msg.getConversationId() + "]");
            }
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }

}
