package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

public class ClientPropagateMessageBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -4865095272921712993L;

    private static final Logger log = LogManager.getLogger();

    private static final String LANGUAGE = "JSON";

    private static final int PERFORMATIVE = ACLMessage.PROPAGATE;

    private static final String CONVERSATION_ID = "client-message";

    public ClientPropagateMessageBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    public void onTick() {
        ClientAgent agent = (ClientAgent) myAgent;
        List<AID> transmitters = agent.getAgentsInRange();
        if (!transmitters.isEmpty()) {
            // we dont want to send message to all possible transmitters, only to one
            // I choose the first on the list
            AID receiver = transmitters.get(0);
            List<ClientMessage> messages = agent.getClientMessages();
            if (!messages.isEmpty()) {
                // send one message by behaviour cycle or all ?
                // I choosed one by behaviour cycle
                ClientMessage message = messages.remove(0);
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                try {
                    String content = mapper.writeValueAsString(message);
                    ACLMessage aclm = new ACLMessage(PERFORMATIVE);
                    aclm.addReceiver(receiver);
                    aclm.setContent(content);
                    aclm.setLanguage(LANGUAGE);
                    aclm.setConversationId(CONVERSATION_ID);
                    agent.send(aclm);
                    log.info("Wyslalem wiadomosc do Transmitera");
                } catch (JsonProcessingException e) {
                    log.error("Could not parse ClientMessage");
                    // we lost message this way because we removed it from the list
                    // we can add it again agent.queueClientMessage(message)
                    // but do we want message that we cant send?
                }
            }
        }
    }

}
