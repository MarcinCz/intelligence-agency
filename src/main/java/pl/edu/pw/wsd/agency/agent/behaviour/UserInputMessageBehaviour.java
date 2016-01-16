package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.io.IOException;

/**
 * User can send to Client messages which will be propagated by Client as Client message.
 * This behaviour reads that User messages.
 * This can be assumed as User input.
 *
 * @author Adrian Sidor
 */
public class UserInputMessageBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = -565817641845670437L;

    private static final Logger log = LogManager.getLogger();

    private static final String CONVERSATION_ID = "user-input";

    private boolean test = true;
    private long lastSentTime = System.currentTimeMillis();

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(CONVERSATION_ID);
        log.trace("Czekam na wiadomosc od Uzytkownika.");
        ClientAgent agent = (ClientAgent) myAgent;
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
        if (msg != null || (test && lastSentTime + 5000 < System.currentTimeMillis())) {
            try {
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                String content = null;
                if (test) {
                    content = mapper.writeValueAsString(new ClientMessage(new MessageId("client_1", lastSentTime + ""), "client_2", "Hello WSD", System.currentTimeMillis() + 1000l));
                } else {
                    content = msg.getContent();
                }
                ClientMessage cm = mapper.readValue(content, ClientMessage.class);
                agent.queueClientMessage(cm);
                lastSentTime = System.currentTimeMillis();
                log.info("Odebralem wiadomosc od uzytkownika");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            block();
        }

    }

}
